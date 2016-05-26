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

package algostorm.assets

/**
 * A container that maps asset ids to assets.
 *
 * @param T the asset type
 * @property assets the underlying map that associates ids to assets
 */
data class AssetCollection<T : Asset>(private val assets: Map<Int, T>) {
    /**
     * Returns the asset with the given [id].
     *
     * @param id the id of the requested asset
     * @return the requested asset, or `null` if the given [id] doesn't exist in
     * this container
    */
    operator fun get(id: Int): T? = assets[id]

    /**
     * Returns whether this asset collection contains the asset with the given
     * [id].
     *
     * @param id the id of the asset
     * @return `true` if the given asset [id] is contained in this collection,
     * `false` otherwise
     */
    operator fun contains(id: Int): Boolean = id in assets
}
