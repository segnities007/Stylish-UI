package com.segnities007.stylishui.adapters

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class StylishAdapterContractTest {
    @Test
    fun navigationCoversNavigateBackDeepLinkRestoreAndInvalidRoute() {
        val initial = StylishNavigationState("home")
        val selected = StylishNavigationReducer.dispatch(initial, StylishNavigationIntent.Navigate("settings"))
        assertEquals(listOf("home", "settings"), assertIs<StylishAdapterResult.Success<StylishNavigationState>>(selected).value.backStack)
        val back = StylishNavigationReducer.dispatch(
            assertIs<StylishAdapterResult.Success<StylishNavigationState>>(selected).value,
            StylishNavigationIntent.Back,
        )
        assertEquals("home", assertIs<StylishAdapterResult.Success<StylishNavigationState>>(back).value.currentRoute)
        val deepLink = StylishNavigationReducer.dispatch(initial, StylishNavigationIntent.DeepLink("invite"))
        assertEquals(listOf("invite"), assertIs<StylishAdapterResult.Success<StylishNavigationState>>(deepLink).value.backStack)
        val restored = StylishNavigationReducer.dispatch(initial, StylishNavigationIntent.Restore(listOf("home", "profile")))
        assertTrue(assertIs<StylishAdapterResult.Success<StylishNavigationState>>(restored).value.restored)
        val invalid = StylishNavigationReducer.dispatch(initial, StylishNavigationIntent.Navigate("  "))
        assertEquals(
            StylishAdapterErrorCode.InvalidRequest,
            assertIs<StylishAdapterResult.Failure>(invalid).error.code,
        )
    }

    @Test
    fun filePickerCoversSuccessCancellationPermissionAndDuplicateIdentity() = runBlocking {
        val file = StylishPickedFile("stable-1", "report.pdf", "application/pdf", 42)
        val request = StylishFilePickerRequest(
            mimeTypes = listOf("application/pdf"),
            permission = StylishPermissionStatus.Granted,
        )
        val success = InMemoryFilePicker(StylishAdapterResult.Success(listOf(file, file))).pick(request)
        assertEquals(listOf(file), assertIs<StylishAdapterResult.Success<List<StylishPickedFile>>>(success).value)
        val cancelled = InMemoryFilePicker(StylishAdapterResult.Cancelled).pick(request)
        assertIs<StylishAdapterResult.Cancelled>(cancelled)
        val denied = InMemoryFilePicker(StylishAdapterResult.Success(listOf(file))).pick(
            request.copy(permission = StylishPermissionStatus.Denied),
        )
        assertEquals(
            StylishAdapterErrorCode.PermissionDenied,
            assertIs<StylishAdapterResult.Failure>(denied).error.code,
        )
        val invalidIdentity = InMemoryFilePicker(
            StylishAdapterResult.Success(listOf(file.copy(id = ""))),
        ).pick(request)
        assertEquals(
            StylishAdapterErrorCode.InvalidRequest,
            assertIs<StylishAdapterResult.Failure>(invalidIdentity).error.code,
        )
    }

    @Test
    fun imageAdapterCoversSuccessAndDecodeFailure() = runBlocking {
        val resource = StylishImageResource("avatar-1", "image/png", 64, 64)
        val success = InMemoryImageAdapter(StylishAdapterResult.Success(resource)).load(StylishImageRequest("avatar-1"))
        assertEquals(resource, assertIs<StylishAdapterResult.Success<StylishImageResource>>(success).value)
        val invalid = InMemoryImageAdapter(StylishAdapterResult.Success(resource.copy(widthPx = 0)))
            .load(StylishImageRequest("avatar-1"))
        assertEquals(
            StylishAdapterErrorCode.InvalidRequest,
            assertIs<StylishAdapterResult.Failure>(invalid).error.code,
        )
        val failed = InMemoryImageAdapter(
            StylishAdapterResult.Failure(
                StylishAdapterError(StylishAdapterErrorCode.DecodeFailed, "decode failed"),
            ),
        ).load(StylishImageRequest("avatar-1"))
        assertEquals(StylishAdapterErrorCode.DecodeFailed, assertIs<StylishAdapterResult.Failure>(failed).error.code)
    }

    @Test
    fun qrAdapterCoversSuccessInvalidInputAndCancellation() = runBlocking {
        val matrix = StylishQrMatrix(size = 2, modules = listOf(true, false, false, true))
        val success = InMemoryQrEncoder(StylishAdapterResult.Success(matrix)).encode(StylishQrRequest("hello"))
        assertEquals(listOf(listOf(true, false), listOf(false, true)), assertIs<StylishAdapterResult.Success<StylishQrMatrix>>(success).value.rows())
        val invalid = InMemoryQrEncoder(StylishAdapterResult.Success(matrix)).encode(StylishQrRequest(""))
        assertEquals(
            StylishAdapterErrorCode.InvalidRequest,
            assertIs<StylishAdapterResult.Failure>(invalid).error.code,
        )
        val cancelled = InMemoryQrEncoder(StylishAdapterResult.Cancelled).encode(StylishQrRequest("hello"))
        assertIs<StylishAdapterResult.Cancelled>(cancelled)
        Unit
    }

    @Test
    fun flowAdapterEmitsLoadingContentAndErrorWithoutLeakingCancellation() = runBlocking {
        val states = mutableListOf<StylishStreamState<Int>>()
        val job = StylishFlowAdapter(flow { emit(1); emit(2) }).collect(this) { states += it }
        job.join()
        assertIs<StylishStreamState.Loading>(states.first())
        assertEquals(listOf(1, 2), states.filterIsInstance<StylishStreamState.Content<Int>>().map { it.value })

        val errors = mutableListOf<StylishStreamState<Int>>()
        val failed = StylishFlowAdapter<Int>(flow { error("offline") }).collect(this) { errors += it }
        failed.join()
        assertEquals(
            StylishAdapterErrorCode.Transport,
            assertIs<StylishStreamState.Error>(errors.last()).error.code,
        )
    }
}
