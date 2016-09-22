package com.aheidelbacher.algostorm.engine.tiled

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

/**
 * A path to a file.
 *
 * @param string the path of this file
 */
class File @JsonCreator constructor(string: String) {
    /**
     * The relative or absolute path of this file.
     */
    val path: String = string

    override fun equals(other: Any?): Boolean =
            other is File && path == other.path

    override fun hashCode(): Int = path.hashCode()

    @JsonValue override fun toString(): String = path
}
