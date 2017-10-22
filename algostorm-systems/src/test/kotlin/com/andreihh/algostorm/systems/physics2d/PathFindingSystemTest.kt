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

package com.andreihh.algostorm.systems.physics2d

import com.andreihh.algostorm.systems.physics2d.PathFindingSystem.Companion.findPath
import com.andreihh.algostorm.systems.physics2d.geometry2d.Direction
import org.junit.Test
import kotlin.test.assertEquals

class PathFindingSystemTest {
    fun makeMap(vararg rows: String): (Position) -> Boolean = { p ->
        p.y < 0 || rows.size <= p.y || p.x < 0 || rows[p.y].length <= p.x
                || rows[p.y][p.x] !in ".SD"
    }

    fun findSymbolInMap(symbol: Char, vararg rows: String): Position {
        val y = rows.indices.single { symbol in rows[it] }
        val x = rows[y].indices.single { rows[y][it] == symbol }
        return Position(x, y)
    }

    fun testMap(expectedPathLength: Int?, vararg rows: String) {
        val source = findSymbolInMap('S', *rows)
        val destination = findSymbolInMap('D', *rows)
        val path = findPath(
                source = source,
                destination = destination,
                directions = Direction.ORDINAL,
                isCollider = makeMap(*rows)
        )
        assertEquals(expectedPathLength, path?.size)
        if (path != null) {
            var (x, y) = source
            assertEquals('S', rows[y][x])
            path.forEachIndexed { i, direction ->
                x += direction.dx
                y += direction.dy
                if (i + 1 < path.size) {
                    assertEquals('.', rows[y][x])
                }
            }
            assertEquals('D', rows[y][x])
        }
    }

    @Test fun testFindPathLengthZero() {
        val path = findPath(Position(0, 0), Position(0, 0), Direction.ORDINAL) {
            it != Position(0, 0)
        }
        assertEquals(emptyList(), path)
    }

    @Test fun testFindPathLengthOne() {
        testMap(1, "SD")
    }

    @Test fun testFindPathInaccessible() {
        testMap(null, "SXD")
    }

    @Test fun testFindPath() {
        testMap(
                4,
                "SX..",
                "..X.",
                "..X.",
                "X..D"
        )
    }

    @Test fun testFindPath2() {
        testMap(
                11,
                ".......",
                "..SXXX.",
                ".XXX...",
                ".XX.XXX",
                ".X.XXD.",
                ".X...X."
        )
    }

    @Test fun testFindPath3() {
        testMap(
                9,
                ".......",
                "..SXXX.",
                ".XXX...",
                ".XX.XXX",
                ".X.XXD.",
                ".....X."
        )
    }
}
