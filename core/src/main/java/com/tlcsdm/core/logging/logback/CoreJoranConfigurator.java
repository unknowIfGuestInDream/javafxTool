package com.tlcsdm.core.logging.logback;

import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.ElementSelector;
import ch.qos.logback.core.joran.spi.RuleStore;
import ch.qos.logback.core.model.Model;
import ch.qos.logback.core.model.processor.DefaultProcessor;

/**
 * Extended version of the Logback {@link JoranConfigurator} that adds additional rules.
 *
 * @author unknowIfGuestInDream
 */
public class CoreJoranConfigurator extends JoranConfigurator {

    CoreJoranConfigurator() {
    }

    @Override
    protected void sanityCheck(Model topModel) {
        super.sanityCheck(topModel);
        performCheck(new WorkEnvIfNestedWithinSecondPhaseElementSanityChecker(), topModel);
    }

    @Override
    protected void addModelHandlerAssociations(DefaultProcessor defaultProcessor) {
        defaultProcessor.addHandler(WorkEnvModel.class,
            (handlerContext, handlerMic) -> new WorkEnvModelHandler(this.context));
        super.addModelHandlerAssociations(defaultProcessor);
    }

    @Override
    public void addElementSelectorAndActionAssociations(RuleStore ruleStore) {
        super.addElementSelectorAndActionAssociations(ruleStore);
        ruleStore.addRule(new ElementSelector("*/workEnv"), WorkEnvAction::new);
        ruleStore.addTransparentPathPart("workEnv");
    }

    @Override
    public void processModel(Model model) {
        super.processModel(model);
    }

}
