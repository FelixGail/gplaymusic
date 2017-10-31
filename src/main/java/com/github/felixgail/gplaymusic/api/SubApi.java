package com.github.felixgail.gplaymusic.api;

import javax.validation.constraints.NotNull;

public interface SubApi {
    @NotNull
    GPlayMusic getMainApi();
}
