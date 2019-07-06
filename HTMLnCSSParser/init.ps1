Write-Output "Initializing all environment parameters", ""

$a = @{
    Root_Path = $PWD.Path;
    Java_Files = "\src\java";
    CSS_Files = "\src\resources\web\css"
} | ConvertTo-Json

Write-Output $a
Write-Output "Reflecting changes to internal\env.json"
# Saving the data in JSON file
$a | Set-Content '.\internal\env.json'