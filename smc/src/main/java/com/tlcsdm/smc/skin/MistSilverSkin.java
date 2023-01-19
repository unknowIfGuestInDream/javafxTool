package com.tlcsdm.smc.skin;

import java.util.Objects;

import com.tlcsdm.frame.FXSamplerConfiguration;

public class MistSilverSkin implements FXSamplerConfiguration {

    @Override
    public String getSceneStylesheet() {
        return Objects.requireNonNull(getClass().getResource("/com/tlcsdm/smc/fxsampler/mistSilverSkin.css"))
                .toExternalForm();
    }

}
