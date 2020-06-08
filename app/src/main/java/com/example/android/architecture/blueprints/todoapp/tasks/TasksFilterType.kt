/*
 * Copyright (C) 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.architecture.blueprints.todoapp.tasks

import com.example.android.architecture.blueprints.todoapp.assistant.DeepLink

/**
 * Used with the filter spinner in the tasks list.
 * @param value Id injected with the corresponding Android resource from res folder.
 */
enum class TasksFilterType(val value: String) {
    /**
     * Filters only the active (not completed yet) tasks.
     */
    ACTIVE_TASKS(DeepLink.ACTIVE_TASKS_PATH),
    /**
     * Filters only the completed tasks.
     */
    COMPLETED_TASKS(DeepLink.COMPLETED_TASKS_PATH),
    /**
     * Do not filter tasks.
     */
    ALL_TASKS(DeepLink.ALL_TASKS_PATH);

    companion object {

        /**
         * @param type of task used to filter tasks from the model view and displayed by the
         * task list.
         * @return a TasksFilterType.Type that matches the given name
         */
        fun find(type: String?): TasksFilterType {
            return values().find { it.value.equals(other = type, ignoreCase = true) } ?: ALL_TASKS
        }
    }
}
