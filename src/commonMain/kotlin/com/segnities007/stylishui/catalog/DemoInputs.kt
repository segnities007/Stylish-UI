package com.segnities007.stylishui.catalog

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.segnities007.stylishui.components.atoms.StylishFilledTextField
import com.segnities007.stylishui.components.atoms.StylishFormTextField
import com.segnities007.stylishui.components.atoms.StylishNumberInput
import com.segnities007.stylishui.components.atoms.StylishOutlinedTextField
import com.segnities007.stylishui.components.atoms.StylishPinInput
import com.segnities007.stylishui.components.molecules.StylishAutocomplete
import com.segnities007.stylishui.components.molecules.StylishDatePickerField
import com.segnities007.stylishui.components.molecules.StylishEditable
import com.segnities007.stylishui.components.molecules.StylishFormField
import com.segnities007.stylishui.components.molecules.StylishTimePicker
import kotlinx.datetime.LocalDate

/**
 * Returns all input-related demo components for the catalog.
 */
@OptIn(ExperimentalMaterial3Api::class)
internal fun getInputDemos(): List<DemoComponent> = listOf(
    DemoComponent(
        name = "Form text field",
        category = DemoCategory.Inputs,
        code = """StylishFormTextField(
    value = value,
    onValueChange = { value = it },
    label = "メールアドレス",
    isError = isError,
    errorMessage = if (isError) "形式が正しくありません" else null,
)""",
        preview = {
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
        },
    ),
    DemoComponent(
        name = "Filled text field",
        category = DemoCategory.Inputs,
        code = """StylishFilledTextField(
    value = value,
    onValueChange = { value = it },
    label = "ユーザー名",
)""",
        preview = {
            var value by remember { mutableStateOf("") }
            StylishFilledTextField(
                value = value,
                onValueChange = { value = it },
                label = "ユーザー名",
                placeholder = "入力してください",
            )
        },
    ),
    DemoComponent(
        name = "Outlined text field",
        category = DemoCategory.Inputs,
        code = """StylishOutlinedTextField(
    value = value,
    onValueChange = { value = it },
    label = { Text("コメント") },
)""",
        preview = {
            var value by remember { mutableStateOf("") }
            StylishOutlinedTextField(
                value = value,
                onValueChange = { value = it },
                label = { Text("コメント") },
                placeholder = { Text("入力してください") },
            )
        },
    ),
    DemoComponent(
        name = "Autocomplete",
        category = DemoCategory.Inputs,
        code = """StylishAutocomplete(
    value = value,
    onValueChange = { value = it },
    options = listOf("Stylish UI", "Compose Multiplatform", "Material 3"),
    label = "検索",
)""",
        preview = {
            var value by remember { mutableStateOf("") }
            StylishAutocomplete(
                value = value,
                onValueChange = { value = it },
                options = listOf("Stylish UI", "Compose Multiplatform", "Material 3", "Kotlin", "KMP"),
                label = "ライブラリ検索",
            )
        },
    ),
    DemoComponent(
        name = "Date picker",
        category = DemoCategory.Inputs,
        code = """StylishDatePickerField(
    value = date,
    onValueChange = { date = it },
    label = "日付",
    confirmLabel = "OK",
    dismissLabel = "キャンセル",
)""",
        preview = {
            var date by remember { mutableStateOf<LocalDate?>(null) }
            StylishDatePickerField(
                value = date,
                onValueChange = { date = it },
                label = "日付",
                confirmLabel = "OK",
                dismissLabel = "キャンセル",
                placeholder = "日付を選択",
            )
        },
    ),
    DemoComponent(
        name = "Time picker",
        category = DemoCategory.Inputs,
        code = """StylishTimePicker(state = rememberTimePickerState())""",
        preview = {
            val state = rememberTimePickerState()
            StylishTimePicker(state = state)
        },
    ),
    DemoComponent(
        name = "Number input",
        category = DemoCategory.Inputs,
        code = """StylishNumberInput(
    value = value,
    onValueChange = { value = it },
    label = "台数",
    range = 1..10,
)""",
        preview = {
            var value by remember { mutableStateOf(5) }
            StylishNumberInput(
                value = value,
                onValueChange = { value = it },
                label = "台数",
                range = 1..10,
            )
        },
    ),
    DemoComponent(
        name = "Pin input",
        category = DemoCategory.Inputs,
        code = """StylishPinInput(
    value = value,
    onValueChange = { value = it },
)""",
        preview = {
            var value by remember { mutableStateOf("") }
            StylishPinInput(value = value, onValueChange = { value = it })
        },
    ),
    DemoComponent(
        name = "Editable",
        category = DemoCategory.Inputs,
        code = """StylishEditable(
    value = value,
    onValueChange = { value = it },
)""",
        preview = {
            var value by remember { mutableStateOf("Stylish UI") }
            StylishEditable(value = value, onValueChange = { value = it })
        },
    ),
    DemoComponent(
        name = "Form field",
        category = DemoCategory.Inputs,
        code = """StylishFormField(
    label = "車両名",
    required = true,
    supportingText = "登録する車両の名前を入力してください。",
) {
    StylishFormTextField(value = value, onValueChange = {}, label = "車両名")
}""",
        preview = {
            var value by remember { mutableStateOf("") }
            StylishFormField(
                label = "車両名",
                required = true,
                supportingText = "登録する車両の名前を入力してください。",
            ) {
                StylishFormTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = "車両名",
                )
            }
        },
    ),
)
