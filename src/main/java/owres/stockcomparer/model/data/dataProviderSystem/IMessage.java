package owres.stockcomparer.model.data.dataProviderSystem;

/**
 * Shared interface used by every filter in the DataProviderSystem pipeline.
 */
public interface IMessage {

    /**
     * Process the pipeline request and either forward it to the next filter
     * or reject it by calling request.reject(reason).
     *
     * @param request The shared data carrier for this pipeline run
     */
    void transmit(PipelineRequest request);
}