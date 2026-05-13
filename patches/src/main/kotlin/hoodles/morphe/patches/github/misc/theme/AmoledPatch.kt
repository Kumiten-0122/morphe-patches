package hoodles.morphe.patches.github.misc.theme

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.instructions
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch
import app.morphe.util.addInstructionsAtControlFlowLabel
import app.morphe.util.asSequence
import app.morphe.util.findElementByAttributeValue
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.formats.Instruction35c
import hoodles.morphe.patches.all.manifest.debug.enableDebugPatch
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory
import kotlin.random.Random

val bytecodeOverrides = bytecodePatch {
    execute {
        val blackLong = "0xFF00000000000000L"
        FunctionalColorsCtorFingerprint.method.addInstructions(0, """
            const-wide p3, $blackLong
        """.trimIndent())

        SetNavigationBarContrastFingerprint.matchAll().forEach {
            val setContrastIndex = it.instructionMatches.first().index
            val setContrastReg = it.method.getInstruction<Instruction35c>(setContrastIndex).registerD

            Opcode.INVOKE_VIRTUAL
            it.method.addInstructionsAtControlFlowLabel(setContrastIndex, """
                const/4 v$setContrastReg, 0x0
            """.trimIndent())
        }
    }
}

@Suppress("unused")
val amoledPatch = resourcePatch(
    name = "AMOLED dark theme",
    description = "Changes the default dark theme to use true blacks for AMOLED screens.",
    default = false
) {
    dependsOn(bytecodeOverrides)

    compatibleWith(Compatibility(
        name = "GitHub",
        packageName = "com.github.android",
        appIconColor = 0x000000,
        targets = listOf(AppTarget("1.255.0"))
    ))

    execute {
        val trueBlack = "#000000"
        val xpath = XPathFactory.newInstance().newXPath()

        document("res/values-night/colors.xml").use { document ->
            val blackColors = listOf(
                "backgroundInset",
                "backgroundSecondary",
            )

            val colors = document.getElementsByTagName("color")
            blackColors.forEach {
                colors.findElementByAttributeValue("name", it)?.textContent = trueBlack
            }
        }
    }
}