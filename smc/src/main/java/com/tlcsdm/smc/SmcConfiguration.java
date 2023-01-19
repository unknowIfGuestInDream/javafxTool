package com.tlcsdm.smc;

import java.util.Objects;

import com.tlcsdm.frame.FXSamplerConfiguration;

public class SmcConfiguration implements FXSamplerConfiguration {

    @Override
    public String getSceneStylesheet() {
        return Objects.requireNonNull(getClass().getResource("/com/tlcsdm/smc/fxsampler/fxsampler.css"))
                .toExternalForm();
    }

}
