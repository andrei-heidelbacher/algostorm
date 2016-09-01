[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.graphics2d](../index.md) / [Matrix](.)

# Matrix

`class Matrix`

Used to apply a sequence of transformations to a bitmap before drawing to the
canvas.

### Functions

| Name | Summary |
|---|---|
| [copy](copy.md) | `fun copy(): Matrix` |
| [get](get.md) | `operator fun get(index: Int): Float`<br>`operator fun get(row: Int, column: Int): Float` |
| [getRawValues](get-raw-values.md) | `fun getRawValues(): FloatArray` |
| [getValues](get-values.md) | `fun getValues(): FloatArray` |
| [postConcat](post-concat.md) | `fun postConcat(other: Matrix): Matrix` |
| [postRotate](post-rotate.md) | `fun postRotate(degrees: Float): Matrix`<br>`fun postRotate(degrees: Float, px: Float, py: Float): Matrix` |
| [postScale](post-scale.md) | `fun postScale(sx: Float, sy: Float): Matrix` |
| [postTranslate](post-translate.md) | `fun postTranslate(dx: Float, dy: Float): Matrix` |
| [preScale](pre-scale.md) | `fun preScale(sx: Float, sy: Float): Matrix` |
| [reset](reset.md) | `fun reset(): Unit` |
| [toString](to-string.md) | `fun toString(): String` |

### Companion Object Properties

| Name | Summary |
|---|---|
| [PERSPECTIVE_0](-p-e-r-s-p-e-c-t-i-v-e_0.md) | `const val PERSPECTIVE_0: Int` |
| [PERSPECTIVE_1](-p-e-r-s-p-e-c-t-i-v-e_1.md) | `const val PERSPECTIVE_1: Int` |
| [PERSPECTIVE_2](-p-e-r-s-p-e-c-t-i-v-e_2.md) | `const val PERSPECTIVE_2: Int` |
| [SCALE_X](-s-c-a-l-e_-x.md) | `const val SCALE_X: Int` |
| [SCALE_Y](-s-c-a-l-e_-y.md) | `const val SCALE_Y: Int` |
| [SKEW_X](-s-k-e-w_-x.md) | `const val SKEW_X: Int` |
| [SKEW_Y](-s-k-e-w_-y.md) | `const val SKEW_Y: Int` |
| [TRANSLATE_X](-t-r-a-n-s-l-a-t-e_-x.md) | `const val TRANSLATE_X: Int` |
| [TRANSLATE_Y](-t-r-a-n-s-l-a-t-e_-y.md) | `const val TRANSLATE_Y: Int` |

### Companion Object Functions

| Name | Summary |
|---|---|
| [identity](identity.md) | `fun identity(): Matrix`<br>Returns the identity matrix. |
