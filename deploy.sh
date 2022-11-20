#!/bin/bash
gcloud functions deploy function-investpeer-prosper \
--entry-point org.springframework.cloud.function.adapter.gcp.GcfJarLauncher \
--runtime java17 \
--trigger-topic investpeer-prosper-topic \
--source target/deploy \
--memory 512MB