package com.tenkiv.tekdaqc

import com.tenkiv.tekdaqc.communication.message.IDigitalChannelListener
import com.tenkiv.tekdaqc.hardware.ATekdaqc
import com.tenkiv.tekdaqc.locator.Locator
import com.tenkiv.tekdaqc.locator.OnTargetTekdaqcFound
import com.tenkiv.tekdaqc.locator.OnTekdaqcDiscovered

/**
 * Created by tenkiv on 5/5/17.
 */
class LocatorTest {
    init {

        var assert = false

        Locator.instance.addLocatorListener(object : OnTekdaqcDiscovered {
            override fun onTekdaqcResponse(board: ATekdaqc) {
                println("Found ${board.serialNumber}")
            }

            override fun onTekdaqcFirstLocated(board: ATekdaqc) {
            }

            override fun onTekdaqcNoLongerLocated(board: ATekdaqc) {
            }

        })

        Locator.instance.searchForSpecificTekdaqcs(object : OnTargetTekdaqcFound {
            override fun onTargetFound(tekdaqc: ATekdaqc) {
                println(tekdaqc.serialNumber)
                tekdaqc.addDigitalChannelListener(IDigitalChannelListener { input, data -> assert = true }, tekdaqc.getDigitalInput(0))

                tekdaqc.getDigitalInput(0).activate()

                tekdaqc.sample(20)

            }

            override fun onTargetFailure(serial: String, flag: OnTargetTekdaqcFound.FailureFlag) {
            }

            override fun onAllTargetsFound(tekdaqcs: Set<ATekdaqc>) {
            }

        }, 10000, true, ATekdaqc.AnalogScale.ANALOG_SCALE_5V, "00000000000000000000000000000012")

        Thread.sleep(20000)

        assert(assert)
    }
}