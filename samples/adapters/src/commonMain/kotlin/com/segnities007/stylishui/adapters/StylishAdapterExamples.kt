package com.segnities007.stylishui.adapters

/**
 * Deterministic host stubs used by the copy-ready sample and contract tests. Production hosts
 * replace these with Android SAF, UIImage/ImageDecoder, browser File API, or desktop APIs.
 */
public class InMemoryFilePicker(
    private val outcome: StylishAdapterResult<List<StylishPickedFile>>,
) : StylishFilePickerAdapter {
    override suspend fun pick(request: StylishFilePickerRequest): StylishAdapterResult<List<StylishPickedFile>> =
        when (outcome) {
            StylishAdapterResult.Cancelled -> StylishAdapterResult.Cancelled
            is StylishAdapterResult.Failure -> outcome
            is StylishAdapterResult.Success -> validatePickedFiles(request, outcome.value)
        }
}

/** Deterministic image adapter used to test placeholder, success, and decode-failure states. */
public class InMemoryImageAdapter(
    private val outcome: StylishAdapterResult<StylishImageResource>,
) : StylishImageAdapter {
    override suspend fun load(request: StylishImageRequest): StylishAdapterResult<StylishImageResource> =
        when (outcome) {
            StylishAdapterResult.Cancelled -> StylishAdapterResult.Cancelled
            is StylishAdapterResult.Failure -> outcome
            is StylishAdapterResult.Success -> validateImageResource(outcome.value)
        }
}

/** Deterministic QR adapter used by the platform-independent sample. */
public class InMemoryQrEncoder(
    private val outcome: StylishAdapterResult<StylishQrMatrix>,
) : StylishQrEncoderAdapter {
    override suspend fun encode(request: StylishQrRequest): StylishAdapterResult<StylishQrMatrix> =
        when (outcome) {
            StylishAdapterResult.Cancelled -> StylishAdapterResult.Cancelled
            is StylishAdapterResult.Failure -> outcome
            is StylishAdapterResult.Success -> validateQrResult(request, outcome.value)
        }
}
