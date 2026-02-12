$headers = @{
    'Content-Type'  = 'application/json'
    'Authorization' = 'Bearer <JWT_TOKEN_PLACEHOLDER>'
}

$products = Get-Content -Raw -Path "products_seed.json" | ConvertFrom-Json

foreach ($p in $products) {
    Write-Host "Seeding product: $($p.name)"
    try {
        $body = $p | ConvertTo-Json -Depth 10
        $response = Invoke-RestMethod -Uri 'http://localhost:27001/api/products' -Method Post -Headers $headers -Body $body
        Write-Host "Successfully seeded $($p.name)"
    }
    catch {
        Write-Error "Failed to seed $($p.name): $_"
    }
}
