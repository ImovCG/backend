package com.imovcg.back.config;

import com.imovcg.back.service.CompletudeBackfillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/** Executa o backfill idempotente de completude durante a inicialização. */
@Component
@ConditionalOnProperty(
        name = "imovcg.completude.backfill-enabled",
        havingValue = "true",
        matchIfMissing = true)
public class CompletudeBackfillRunner implements ApplicationRunner {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(CompletudeBackfillRunner.class);

    private final CompletudeBackfillService backfillService;

    public CompletudeBackfillRunner(CompletudeBackfillService backfillService) {
        this.backfillService = backfillService;
    }

    @Override
    public void run(ApplicationArguments args) {
        int atualizados = backfillService.executar();
        if (atualizados > 0) {
            LOGGER.info("Backfill de completude atualizou {} imóvel(is)", atualizados);
        }
    }
}
