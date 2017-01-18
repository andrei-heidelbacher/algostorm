/*
 * Copyright 2017 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

import com.aheidelbacher.algostorm.core.event.Event
import com.aheidelbacher.algostorm.core.event.Request
import com.aheidelbacher.algostorm.core.event.Subscribe
import com.aheidelbacher.algostorm.core.event.Subscriber

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind.CLASS
import javax.lang.model.element.ElementKind.METHOD
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.ExecutableType
import javax.lang.model.type.TypeKind.VOID
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic

/**
 * Checks that all subscribers comply to the [Subscribe] annotation contract.
 */
class SubscribeProcessor : AbstractProcessor() {
    private companion object {
        val UNIT: String = Unit::class.java.canonicalName
        val SUBSCRIBE: String = Subscribe::class.java.canonicalName
        val EVENT: String = Event::class.java.canonicalName
        val REQUEST: String = Request::class.java.canonicalName
        val SUBSCRIBER: String = Subscriber::class.java.canonicalName
    }

    private lateinit var typeUtils: Types
    private lateinit var elementUtils: Elements
    private lateinit var filer: Filer
    private lateinit var logger: Messager

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        typeUtils = processingEnv.typeUtils
        elementUtils = processingEnv.elementUtils
        filer = processingEnv.filer
        logger = processingEnv.messager
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> =
            mutableSetOf(SUBSCRIBE)

    override fun getSupportedSourceVersion(): SourceVersion =
            SourceVersion.latestSupported()

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
        require(method.kind == METHOD, method) {
            "$method: only methods can be annotated with $SUBSCRIBE!"
        }
        require(Modifier.PUBLIC in method.modifiers, method) {
            "$method: handlers must be public!"
        }
        require(Modifier.FINAL in method.modifiers, method) {
            "$method: handlers must be final!"
        }
        require(Modifier.STATIC !in method.modifiers, method) {
            "$method: handlers must not be static!"
        }
        val executableType = method.asType() as ExecutableType
        val returnsVoid = executableType.returnType.kind == VOID
        val returnsUnit = typeUtils.isSameType(
                executableType.returnType,
                elementUtils.getTypeElement(UNIT).asType()
        )
        require(returnsVoid || returnsUnit, method) {
            "$method: handlers must return void or $UNIT!"
        }
        require(executableType.parameterTypes.size == 1, method) {
            "$method: handlers must receive exactly one parameter!"
        }
        val parameterIsEvent = typeUtils.isSubtype(
                executableType.parameterTypes[0],
                elementUtils.getTypeElement(EVENT).asType()
        )
        val parameterIsRequest = typeUtils.isSubtype(
                executableType.parameterTypes[0],
                typeUtils.erasure(elementUtils.getTypeElement(REQUEST).asType())
        )
        require(parameterIsEvent || parameterIsRequest, method) {
            "$method: handlers must receive a subtype of $EVENT or $REQUEST!"
        }
        val parameterIsNotGeneric = executableType.parameterTypes[0] ==
                typeUtils.erasure(executableType.parameterTypes[0])
        require(parameterIsNotGeneric) {
            "$method: handlers must receive a non-generic parameter!"
        }
    }

    private fun validateEnclosingClass(enclosingClass: Element) {
        require(enclosingClass.kind == CLASS, enclosingClass) {
            "$enclosingClass: enclosing element must be a class!"
        }
        val isSubscriber = typeUtils.isSubtype(
                enclosingClass.asType(),
                elementUtils.getTypeElement(SUBSCRIBER).asType()
        )
        require(isSubscriber, enclosingClass) {
            "$enclosingClass: enclosing class must be a subtype of $SUBSCRIBER!"
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
            logger.printMessage(Diagnostic.Kind.ERROR, e.message, e.element)
        }
        return true
    }
}
