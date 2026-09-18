package com.example

import com.example.data.model.ConnectedDigitalTag
import com.example.data.model.generateDefaultTagFleet
import com.example.data.model.formatRelativeTimestamp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class DigitalTagFleetTest {

    @Test
    fun testDefaultFleetInitialization() {
        val fleet = generateDefaultTagFleet()
        assertTrue("Fleet should have tags", fleet.isNotEmpty())
        assertEquals(17, fleet.size)

        // Verify tags across all 7 counters exist
        val counterCodes = fleet.map { it.counterCode }.toSet()
        assertEquals(7, counterCodes.size)
        assertTrue(counterCodes.contains("CTR-01"))
        assertTrue(counterCodes.contains("CTR-02"))
        assertTrue(counterCodes.contains("CTR-03"))
        assertTrue(counterCodes.contains("CTR-04"))
        assertTrue(counterCodes.contains("CTR-05"))
        assertTrue(counterCodes.contains("CTR-06"))
        assertTrue(counterCodes.contains("CTR-07"))
    }

    @Test
    fun testBatteryHealthAndLifeCalculations() {
        val highBatteryTag = ConnectedDigitalTag(
            tagId = "ESL-TEST-01",
            macAddress = "AA:BB:CC:11:22:33",
            counterCode = "CTR-01",
            counterName = "Counter 1",
            counterZone = "Zone A",
            counterCategory = "Hot Mains",
            dishId = 101L,
            dishName = "Test Dish",
            dishDesc = "Description",
            isVeg = true,
            batteryPct = 95
        )
        assertEquals("Optimal", highBatteryTag.batteryHealth)
        assertEquals("3.20 V", highBatteryTag.batteryVoltage)
        assertTrue(highBatteryTag.estimatedRemainingLife.contains("Years"))

        val lowBatteryTag = highBatteryTag.copy(batteryPct = 18)
        assertEquals("Critical Low", lowBatteryTag.batteryHealth)
        assertEquals("2.65 V (Low Voltage)", lowBatteryTag.batteryVoltage)
        assertTrue(lowBatteryTag.estimatedRemainingLife.contains("Replace"))
    }

    @Test
    fun testRelativeTimestampFormatting() {
        val now = System.currentTimeMillis()
        val justNow = formatRelativeTimestamp(now - 15_000)
        assertTrue(justNow.startsWith("Just now"))

        val fiveMinutesAgo = formatRelativeTimestamp(now - 5 * 60 * 1000)
        assertTrue(fiveMinutesAgo.startsWith("5m ago"))

        val twoHoursAgo = formatRelativeTimestamp(now - 2 * 60 * 60 * 1000)
        assertTrue(twoHoursAgo.startsWith("2h ago"))
    }

    @Test
    fun testTagStateCopy() {
        val original = generateDefaultTagFleet().first()
        val flashed = original.copy(isFlashingLed = true)
        assertTrue(flashed.isFlashingLed)
        assertFalse(original.isFlashingLed)

        val soldOut = original.copy(isSoldOut = true)
        assertTrue(soldOut.isSoldOut)
        assertFalse(original.isSoldOut)
    }
}
