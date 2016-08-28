[algostorm-engine](../../index.md) / [com.aheidelbacher.algostorm.engine.graphics2d](../index.md) / [Color](index.md) / [invoke](.)

# invoke

`operator fun invoke(a: Int, r: Int, g: Int, b: Int): Int`

### Parameters

`a` - the alpha component of the color

`r` - the red component of the color

`g` - the green component of the color

`b` - the blue component of the color

### Exceptions

`IllegalArgumentException` - if any of the provided components is not
in the range `0..255`

**Return**
the requested color in ARGB8888 format

