[algostorm-processor](../../index.md) / [com.aheidelbacher.algostorm.processor](../index.md) / [SubscribeProcessor](.)

# SubscribeProcessor

`class SubscribeProcessor : `[`AbstractProcessor`](http://docs.oracle.com/javase/6/docs/api/javax/annotation/processing/AbstractProcessor.html)

### Constructors

| [&lt;init&gt;](-init-.md) | `SubscribeProcessor()` |

### Functions

| [getSupportedAnnotationTypes](get-supported-annotation-types.md) | `fun getSupportedAnnotationTypes(): MutableSet<String>` |
| [getSupportedSourceVersion](get-supported-source-version.md) | `fun getSupportedSourceVersion(): `[`SourceVersion`](http://docs.oracle.com/javase/6/docs/api/javax/lang/model/SourceVersion.html) |
| [init](init.md) | `fun init(processingEnv: `[`ProcessingEnvironment`](http://docs.oracle.com/javase/6/docs/api/javax/annotation/processing/ProcessingEnvironment.html)`): Unit` |
| [process](process.md) | `fun process(annotations: MutableSet<out `[`TypeElement`](http://docs.oracle.com/javase/6/docs/api/javax/lang/model/element/TypeElement.html)`>, roundEnv: `[`RoundEnvironment`](http://docs.oracle.com/javase/6/docs/api/javax/annotation/processing/RoundEnvironment.html)`): Boolean` |

### Companion Object Properties

| [SUBSCRIBE](-s-u-b-s-c-r-i-b-e.md) | `val SUBSCRIBE: String` |

