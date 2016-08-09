/*
 * Copyright 2016 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aheidelbacher.algostorm.processor

import com.aheidelbacher.algostorm.event.Event
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.ExecutableType
import javax.lang.model.type.TypeKind
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic

class SubscribeProcessor : AbstractProcessor() {
    companion object {
        val SUBSCRIBE: String = Subscribe::class.java.canonicalName
        private val UNIT = Unit::class.java.canonicalName
        private val EVENT = Event::class.java.canonicalName
        private val SUBSCRIBER = Subscriber::class.java.canonicalName
    }

    private lateinit var typeUtils: Types
    private lateinit var elementUtils: Elements
    private lateinit var filer: Filer
    private lateinit var messager: Messager

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        typeUtils = processingEnv.typeUtils
        elementUtils = processingEnv.elementUtils
        filer = processingEnv.filer
        messager = processingEnv.messager
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> =
            mutableSetOf(SUBSCRIBE)

    override fun getSupportedSourceVersion(): SourceVersion =
            SourceVersion.latestSupported()

    private fun error(element: Element?, message: String?) {
        messager.printMessage(Diagnostic.Kind.ERROR, message, element)
    }

    private inline fun require(
            condition: Boolean,
            element: Element,
            message: () -> String
    ) {
        if (!condition) {
            throw ProcessingException(element, message())
        }
    }

    private fun validateMethod(method: Element) {
        require(method.kind == ElementKind.METHOD, method) {
            "Only methods can be annotated with $SUBSCRIBE!"
        }
        require(Modifier.PUBLIC in method.modifiers, method) {
            "Annotated methods must be public!"
        }
        require(Modifier.FINAL in method.modifiers, method) {
            "Annotated methods must be final!"
        }
        require(Modifier.STATIC !in method.modifiers, method) {
            "Annotated methods must not be static!"
        }
        val executableType = method.asType() as ExecutableType
        val returnsVoid = executableType.returnType.kind == TypeKind.VOID
        val returnsUnit = typeUtils.isSameType(
                executableType.returnType,
                elementUtils.getTypeElement(UNIT).asType()
        )
        require(returnsVoid || returnsUnit, method) {
            "Annotated methods must return void or $UNIT!"
        }
        require(executableType.parameterTypes.size == 1, method) {
            "Annotated methods must receive exactly one parameter!"
        }
        val parameterIsEvent = typeUtils.isSubtype(
                executableType.parameterTypes[0],
                elementUtils.getTypeElement(EVENT).asType()
        )
        require(parameterIsEvent, method) {
            "Annotated methods must receive as parameter a subtype of $EVENT!"
        }
    }

    private fun validateEnclosingClass(enclosingClass: Element) {
        require(enclosingClass.kind == ElementKind.CLASS, enclosingClass) {
            "Enclosing element must be a class!"
        }
        val isSubscriber = typeUtils.isSubtype(
                enclosingClass.asType(),
                elementUtils.getTypeElement(SUBSCRIBER).asType()
        )
        require(isSubscriber, enclosingClass) {
            "Enclosing class must be a subtype of $SUBSCRIBER!"
        }
    }

    override fun process(
            annotations: MutableSet<out TypeElement>,
            roundEnv: RoundEnvironment
    ): Boolean {
        try {
            roundEnv.getElementsAnnotatedWith(Subscribe::class.java).forEach {
                validateMethod(it)
                validateEnclosingClass(it.enclosingElement)
            }
        } catch (e: ProcessingException) {
            error(e.element, e.message)
        }
        return true
    }
}
