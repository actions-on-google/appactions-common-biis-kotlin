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
package com.example.android.architecture.blueprints.todoapp.addedittask

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.R.string
import com.example.android.architecture.blueprints.todoapp.assertSnackbarMessage
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeRepository
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for the implementation of [AddEditTaskViewModel].
 */
@ExperimentalCoroutinesApi
class AddEditTaskViewModelTest {

    // Subject under test
    private lateinit var addEditTaskViewModel: AddEditTaskViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var tasksRepository: FakeRepository

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val task = Task("Title1", "Description1")

    @Before
    fun setupViewModel() {
        // We initialise the repository with no tasks
        tasksRepository = FakeRepository()

        // Create class under test
        addEditTaskViewModel = AddEditTaskViewModel(tasksRepository)
    }

    @Test
    fun saveNewTaskToRepository_showsSuccessMessageUi() {
        val newTitle = "New Task Title"
        val newDescription = "Some Task Description"
        (addEditTaskViewModel).apply {
            title.value = newTitle
            description.value = newDescription
        }
        addEditTaskViewModel.saveTask()

        val newTask = tasksRepository.tasksServiceData.values.first()

        // Then a task is saved in the repository and the view updated
        assertThat(newTask.title).isEqualTo(newTitle)
        assertThat(newTask.description).isEqualTo(newDescription)
    }

    @Test
    fun loadTasks_loading() {
        // Pause dispatcher so we can verify initial values
        mainCoroutineRule.pauseDispatcher()

        // Load the task in the viewmodel
        addEditTaskViewModel.start(task.id)

        // Then progress indicator is shown
        assertThat(addEditTaskViewModel.dataLoading.getOrAwaitValue()).isTrue()

        // Execute pending coroutines actions
        mainCoroutineRule.resumeDispatcher()

        // Then progress indicator is hidden
        assertThat(addEditTaskViewModel.dataLoading.getOrAwaitValue()).isFalse()
    }

    @Test
    fun loadTasks_taskShown() {
        // Add task to repository
        tasksRepository.addTasks(task)

        // Load the task with the viewmodel
        addEditTaskViewModel.start(task.id)

        // Verify a task is loaded
        assertThat(addEditTaskViewModel.title.getOrAwaitValue()).isEqualTo(task.title)
        assertThat(addEditTaskViewModel.description.getOrAwaitValue()).isEqualTo(task.description)
        assertThat(addEditTaskViewModel.dataLoading.getOrAwaitValue()).isFalse()
    }

    @Test
    fun saveNewTaskToRepository_emptyTitle_error() {
        saveTaskAndAssertSnackbarError("", "Some Task Description")
    }

    @Test
    fun saveNewTaskToRepository_nullTitle_error() {
        saveTaskAndAssertSnackbarError(null, "Some Task Description")
    }

    @Test
    fun saveNewTaskToRepository_emptyDescription_error() {
        saveTaskAndAssertSnackbarError("Title", "")
    }

    @Test
    fun saveNewTaskToRepository_nullDescription_error() {
        saveTaskAndAssertSnackbarError("Title", null)
    }

    @Test
    fun saveNewTaskToRepository_nullDescriptionNullTitle_error() {
        saveTaskAndAssertSnackbarError(null, null)
    }

    @Test
    fun saveNewTaskToRepository_emptyDescriptionEmptyTitle_error() {
        saveTaskAndAssertSnackbarError("", "")
    }

    private fun saveTaskAndAssertSnackbarError(title: String?, description: String?) {
        (addEditTaskViewModel).apply {
            this.title.value = title
            this.description.value = description
        }

        // When saving an incomplete task
        addEditTaskViewModel.saveTask()

        // Then the snackbar shows an error
        assertSnackbarMessage(addEditTaskViewModel.snackbarText, string.empty_task_message)
    }
}
