#!/bin/bash
gcloud functions deploy function-investpeer-prosper \
--gen2 \
--region=us-central1 \
--runtime java21 \
--trigger-topic investpeer-prosper-topic \
--source target/deploy \
--entry-point org.springframework.cloud.function.adapter.gcp.GcfJarLauncher \
--memory 512MB