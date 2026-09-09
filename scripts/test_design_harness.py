import importlib.util
from pathlib import Path
import unittest

spec = importlib.util.spec_from_file_location(
    "harness", Path(__file__).with_name("verify-design-harness.py"),
)
harness = importlib.util.module_from_spec(spec)
spec.loader.exec_module(harness)


class HarnessTest(unittest.TestCase):
    def test_valid_screen_and_alias(self):
        failures, screen = harness.audit(
            "import com.segnities007.stylishui.components.patterns.StylishScreen as Screen\n"
            "fun UI() { Screen(title, state, onEvent) }",
        )
        self.assertEqual([], failures)
        self.assertTrue(screen)

    def test_material_import_alias_and_qualified_call(self):
        for source in (
            "import androidx.compose.material3.Text as T\nfun UI() { T(\"Hi\") }",
            "fun UI() { androidx.compose.material3.Text(\"Hi\") }",
            'fun UI() { val x = "${androidx.compose.material3.MaterialTheme.colorScheme}" }',
            "import androidx.compose.material.*",
        ):
            with self.subTest(source=source):
                self.assertTrue(harness.audit(source)[0])

    def test_unapproved_composition_and_wildcards(self):
        for source in (
            "import androidx.compose.foundation.layout.Row",
            "import com.segnities007.stylishui.components.atoms.StylishText",
            "import androidx.compose.runtime.*",
            "import example.CustomRenderer",
        ):
            self.assertTrue(harness.audit(source)[0])

    def test_comments_and_literal_do_not_count_as_screen(self):
        issues, screen = harness.audit(
            '/* outer /* nested */ androidx.compose.material3.Text */\n'
            'val x = "StylishScreen(title, state, onEvent)"\n// StylishScreen()',
        )
        self.assertEqual([], issues)
        self.assertFalse(screen)

    def test_flexible_profile_accepts_library_components(self):
        issues, _ = harness.audit(
            "import com.segnities007.stylishui.components.atoms.StylishText\n"
            "import androidx.compose.ui.Modifier",
            strict=False,
        )
        self.assertEqual([], issues)


if __name__ == "__main__":
    unittest.main()
