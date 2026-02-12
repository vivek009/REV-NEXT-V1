$headers = @{
    'Content-Type'  = 'application/json'
    'Authorization' = 'Bearer <JWT_TOKEN_PLACEHOLDER>'
}

$families = @(
    @{ name = 'Office Solutions' },
    @{ name = 'Home Living' },
    @{ name = 'Precision Hardware' }
)

foreach ($f in $families) {
    $body = $f | ConvertTo-Json
    $response = Invoke-RestMethod -Uri 'http://localhost:27001/api/product-families' -Method Post -Headers $headers -Body $body
    Write-Output ($response | ConvertTo-Json)
}
