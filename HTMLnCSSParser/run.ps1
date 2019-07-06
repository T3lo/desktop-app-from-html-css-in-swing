./compile.bat
# $a = Get-Content '.\internal\env.json' -Raw | ConvertFrom-Json
Set-Location build
# $file = $a.Root_Path + $a.CSS_Files + "\style.css"
# java CSSParser $file
Write-Output "##########################################"
java HTMLParser
Write-Output "##########################################"
Write-Output "##########################################"
Write-Output "##########################################"
Write-Output "##########################################"
java RunTree