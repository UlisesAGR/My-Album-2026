/*
 * Confetti.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.utils.ui

import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit

fun KonfettiView.startParty() {
    this.start(
        Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0x388E3C, 0x9CCC65, 0xF9A825, 0xFFFFFF),
            emitter = Emitter(duration = 200, TimeUnit.MILLISECONDS).max(100),
            position = Position.Relative(0.5, 0.1),
        )
    )
}
