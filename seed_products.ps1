$token = "<JWT_TOKEN_PLACEHOLDER>"
$products = Get-Content products_seed.json | ConvertFrom-Json

foreach ($p in $products) {
    $payload = @{
        name        = $p.name
        description = $p.description
        sku         = $p.sku
        familyId    = $p.familyId
        imageNames  = @($p.imageName)
        attributes  = $p.attributes
    } | ConvertTo-Json -Depth 10

    $response = curl.exe -X POST http://localhost:27001/api/products `
        -H "Content-Type: application/json" `
        -H "Authorization: Bearer $token" `
        -d $payload

    Write-Host "Seeded: $($p.name) - Response: $response"
}
