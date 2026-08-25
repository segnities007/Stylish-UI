package com.segnities007.stylishui.components.atoms

import android.os.Build

internal actual fun isGlassBlurSupported(): Boolean = Build.VERSION.SDK_INT >= 31
