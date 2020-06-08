/*
 * Copyright (C) 2020 Google LLC
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
 *
 */

package com.example.android.architecture.blueprints.todoapp.assistant

/**
 * Static object that defines the different deep-links
 */
object DeepLink {
    const val ALL_TASKS_PATH = "/all-tasks"
    const val ACTIVE_TASKS_PATH = "/active-tasks"
    const val COMPLETED_TASKS_PATH = "/completed-tasks"

    /**
     * Parameter types for the deep-links
     */
    object Params {

        // filter by keyword from /search deeplink declared in nav_graph.xml
        const val Q = "q" // query
    }
}
