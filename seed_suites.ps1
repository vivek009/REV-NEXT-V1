$headers = @{
    'Content-Type' = 'application/json'
    'Authorization' = 'Bearer <JWT_TOKEN_PLACEHOLDER>'
}

# Family IDs
$officeId = "6176f04b-2063-478d-83b3-e216e719535e"
$homeId = "caf4305c-7faf-4a6c-88a2-ae4fac524541"
$hardwareId = "4b2d50ca-e7bb-49ce-a20a-d122432167f2"

# Product IDs
$chairId = "168d7d75-0273-4fee-8bcb-95532bc891a4"
$deskId = "0cdb6308-2401-4c8f-875b-26d853b80e20"
$cabinetId = "9beb8c50-e2e6-4ac5-8b31-3d58589e8b3f"
$sofaId = "d006168e-61ab-4e06-81ac-004c96853a69"
$coffeeTableId = "09487dd2-8fb6-4f51-b897-d3aafc37b7f4"
$handleId = "bd51a6b7-c380-4ae4-aabc-3c54b816370b"
$hingeId = "019ed483-0f76-4e3d-a1da-c3a2b0dc1bc3"
$knobId = "2372aca8-1f5b-448c-9f50-7ae0574d1bba"

# --- Suite Formula 1: Executive Office ---
$formula1 = @{
    familyId = $officeId
    ingredients = @(
        @{ productId = $chairId; quantity = 1; measurementUnit = "Unit" },
        @{ productId = $deskId; quantity = 1; measurementUnit = "Unit" },
        @{ productId = $cabinetId; quantity = 1; measurementUnit = "Unit" }
    )
}

Write-Host "Seeding Formula: Executive Office"
$resp1 = Invoke-RestMethod -Uri 'http://localhost:27001/api/suite-formulas' -Method Post -Headers $headers -Body ($formula1 | ConvertTo-Json -Depth 10)
$formula1Id = $resp1.id

# --- Suite 1 Instance ---
$suite1 = @{
    name = "Premium Executive Office Suite"
    description = "Complete office setup for executives."
    formulaId = $formula1Id
    familyId = $officeId
    remark = "Autumn 2024 Collection"
}
Write-Host "Seeding Suite: Premium Executive Office Suite"
Invoke-RestMethod -Uri 'http://localhost:27001/api/suites' -Method Post -Headers $headers -Body ($suite1 | ConvertTo-Json)

# --- Suite Formula 2: Cozy Living ---
$formula2 = @{
    familyId = $homeId
    ingredients = @(
        @{ productId = $sofaId; quantity = 1; measurementUnit = "Unit" },
        @{ productId = $coffeeTableId; quantity = 1; measurementUnit = "Unit" }
    )
}
Write-Host "Seeding Formula: Cozy Living"
$resp2 = Invoke-RestMethod -Uri 'http://localhost:27001/api/suite-formulas' -Method Post -Headers $headers -Body ($formula2 | ConvertTo-Json -Depth 10)
$formula2Id = $resp2.id

# --- Suite 2 Instance ---
$suite2 = @{
    name = "Urban Living Bundle"
    description = "Perfect combination for small apartments."
    formulaId = $formula2Id
    familyId = $homeId
    remark = "Urban Collection"
}
Write-Host "Seeding Suite: Urban Living Bundle"
Invoke-RestMethod -Uri 'http://localhost:27001/api/suites' -Method Post -Headers $headers -Body ($suite2 | ConvertTo-Json)


# --- Suite Formula 3: Cabinet Hardware ---
$formula3 = @{
    familyId = $hardwareId
    ingredients = @(
        @{ productId = $handleId; quantity = 2; measurementUnit = "Unit" },
        @{ productId = $hingeId; quantity = 4; measurementUnit = "Unit" },
        @{ productId = $knobId; quantity = 4; measurementUnit = "Unit" }
    )
}
Write-Host "Seeding Formula: Cabinet Hardware"
$resp3 = Invoke-RestMethod -Uri 'http://localhost:27001/api/suite-formulas' -Method Post -Headers $headers -Body ($formula3 | ConvertTo-Json -Depth 10)
$formula3Id = $resp3.id

# --- Suite 3 Instance ---
$suite3 = @{
    name = "Full Kitchen Hardware Pack"
    description = "Hardware kit for standard kitchen cabinetry."
    formulaId = $formula3Id
    familyId = $hardwareId
    remark = "Polished Collection"
}
Write-Host "Seeding Suite: Full Kitchen Hardware Pack"
Invoke-RestMethod -Uri 'http://localhost:27001/api/suites' -Method Post -Headers $headers -Body ($suite3 | ConvertTo-Json)
