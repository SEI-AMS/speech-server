#!/usr/bin/env bash

SERVICE_ID=edu.cmu.sei.ams.speech_rec_service
SVM_NAME=speech_osv

capstan build ${SVM_NAME}
cp ~/.capstan/repository/${SVM_NAME}/${SVM_NAME}.qemu ${SERVICE_ID}.qcow2

SVM_FOLDER=speech_svm
rm -r -f ${SVM_FOLDER}
mkdir ${SVM_FOLDER}
cp ~/.capstan/repository/${SVM_NAME}/${SVM_NAME}.qemu ./${SVM_FOLDER}/${SERVICE_ID}.qcow2
cp ./service.json ./${SVM_FOLDER}/

rm -f ${SERVICE_ID}.csvm
cd ./${SVM_FOLDER}
tar -cvf ../${SERVICE_ID}.csvm *.*
cd ..
gzip ${SERVICE_ID}.csvm
mv ${SERVICE_ID}.csvm.gz ${SERVICE_ID}.csvm
rm -r -f ${SVM_FOLDER}
