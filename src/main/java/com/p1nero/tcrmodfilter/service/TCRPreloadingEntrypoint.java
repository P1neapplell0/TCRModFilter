package com.p1nero.tcrmodfilter.service;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.loading.ImmediateWindowHandler;
import org.slf4j.Logger;
import settingdust.preloading_tricks.api.PreloadingEntrypoint;
import settingdust.preloading_tricks.api.PreloadingTricksCallbacks;

import java.util.Set;

public class TCRPreloadingEntrypoint implements PreloadingEntrypoint {

    public static final Logger LOGGER = LogUtils.getLogger();

    public TCRPreloadingEntrypoint() {
        PreloadingTricksCallbacks.SETUP_MODS.register(modManager -> {
            String glVersion = ImmediateWindowHandler.getGLVersion();
            String[] parts = glVersion.split("\\.");
            int major;
            int minor;
            major = Integer.parseInt(parts[0]);
            minor = Integer.parseInt(parts[1]);
            if(major < 4 || (major == 4 && minor < 6)) {
                if(modManager.removeById("acceleratedrendering")){
                    LOGGER.warn("GLVersion < 4.6, acceleratedrendering mod has been removed.");
                }
            }
            if(modManager.getById("epicfight") != null && modManager.removeById("yes_steve_model")){
                LOGGER.warn("Incompatible with epic fight, yes_steve_model mod has been removed.");
            }
            if(modManager.getById("tcrcore") != null) {
                modManager.removeByIds(Set.of("projecte"));
            }
        });
    }

}
