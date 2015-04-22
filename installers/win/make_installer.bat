REM NOTE: This requires the installation of the WiX Toolset (to create the MSI file).
REM heat.exe dir "." -cg SpeechFiles -gg -scom -sreg -sfrag -srd -dr INSTALLLOCATION -out "speechfiles.wxs"
candle speech_msi_installer.wxs
light speech_msi_installer.wixobj
@pause