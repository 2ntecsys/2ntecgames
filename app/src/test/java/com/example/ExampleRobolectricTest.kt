package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("اشكال وظلال", appName)
  }

  @Test
  fun `verify mode selection in viewmodel`() {
    val app = ApplicationProvider.getApplicationContext<android.app.Application>()
    val vm = com.example.viewmodel.GameViewModel(app)
    vm.selectGameMode(com.example.data.GameMode.SHAPES_AND_OBJECTS)
    assertEquals(com.example.data.GameMode.SHAPES_AND_OBJECTS, vm.uiState.value.selectedMode)
    assertEquals("مربع", vm.uiState.value.currentAnimals.firstOrNull()?.arabicName ?: "")
  }
}
