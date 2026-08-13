package com.segnities007.stylishui.catalog

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.segnities007.stylishui.components.atoms.StylishFormTextField
import com.segnities007.stylishui.components.molecules.StylishAutocomplete
import com.segnities007.stylishui.components.molecules.StylishDatePickerField
import kotlinx.datetime.LocalDate

@Composable
internal fun DemoInputs(modifier: Modifier = Modifier) {
    StylishDemoCard(
        title = "Text field",
        description = "入力とエラー状態を確認できます。",
        code = """StylishFormTextField(
    value = value,
    onValueChange = { value = it },
    label = "メールアドレス",
    isError = isError,
    errorMessage = if (isError) "形式が正しくありません" else null,
)""",
        modifier = modifier,
    ) {
        var value by remember { mutableStateOf("") }
        var isError by remember { mutableStateOf(false) }
        StylishFormTextField(
            value = value,
            onValueChange = {
                value = it
                isError = it.isNotEmpty() && !it.contains("@")
            },
            label = "メールアドレス",
            isError = isError,
            errorMessage = if (isError) "「@」を含めて入力してください" else null,
        )
    }

    StylishDemoCard(
        title = "Autocomplete",
        description = "入力に応じて候補を絞り込みます。",
        code = """StylishAutocomplete(
    value = value,
    onValueChange = { value = it },
    options = listOf("Stylish UI", "Compose Multiplatform", "Material 3"),
    label = "検索",
)""",
        modifier = modifier,
    ) {
        var value by remember { mutableStateOf("") }
        StylishAutocomplete(
            value = value,
            onValueChange = { value = it },
            options = listOf("Stylish UI", "Compose Multiplatform", "Material 3", "Kotlin", "KMP"),
            label = "ライブラリ検索",
        )
    }

    StylishDemoCard(
        title = "Date picker",
        description = "日付を選択できます。",
        code = """StylishDatePickerField(
    value = date,
    onValueChange = { date = it },
    label = "日付",
    confirmLabel = "OK",
    dismissLabel = "キャンセル",
)""",
        modifier = modifier,
    ) {
        var date by remember { mutableStateOf<LocalDate?>(null) }
        StylishDatePickerField(
            value = date,
            onValueChange = { date = it },
            label = "日付",
            confirmLabel = "OK",
            dismissLabel = "キャンセル",
            placeholder = "日付を選択",
        )
    }
}
