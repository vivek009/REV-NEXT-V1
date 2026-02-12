$headers = @{
    'Content-Type' = 'application/json'
    'Authorization' = 'Bearer <JWT_TOKEN_PLACEHOLDER>'
}

# Family IDs (Latest)
$officeId = "0bbd43a3-e584-4b07-afc7-90abcc89c465"
$homeId = "340ecc84-386b-4289-9390-bdf636222c0f"
$hardwareId = "26a7b54c-5627-4e9b-8982-2ba352e4c77f"

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
$formula1Id = $null
try {
    $resp1 = Invoke-RestMethod -Uri 'http://localhost:27001/api/suite-formulas' -Method Post -Headers $headers -Body ($formula1 | ConvertTo-Json -Depth 10)
    $formula1Id = $resp1.id
    Write-Host "Created Formula: $formula1Id"
} catch {
    if ($_.Exception.Response.StatusCode -eq "Conflict") {
        Write-Host "Formula already exists (or conflict). Continuing..."
        # If we can't get the ID from the response, we might need a GET, but for now let's hope it's fresh or we find it.
        # Simple fix: fetch if it failed or just handle the error.
        $formulas = Invoke-RestMethod -Uri 'http://localhost:27001/api/suite-formulas' -Method Get -Headers $headers
        $formula1Id = ($formulas | Where-Object { $_.familyId -eq $officeId })[0].id
        Write-Host "Found Existing Formula ID: $formula1Id"
    } else {
        Write-Error "Failed to seed Formula 1: $($_.Exception.Message)"
    }
}

# --- Suite 1 Instance ---
if ($formula1Id) {
    $suite1 = @{
        name = "Premium Executive Office Suite"
        description = "Complete office setup for executives."
        formulaId = $formula1Id
        familyId = $officeId
        remark = "Autumn 2024 Collection"
    }
    Write-Host "Seeding Suite: Premium Executive Office Suite"
    try {
        Invoke-RestMethod -Uri 'http://localhost:27001/api/suites' -Method Post -Headers $headers -Body ($suite1 | ConvertTo-Json)
        Write-Host "Successfully seeded Suite 1"
    } catch {
        Write-Host "Suite 1 might already exist or failed: $($_.Exception.Message)"
    }
}

# --- Suite Formula 2: Cozy Living ---
$formula2 = @{
    familyId = $homeId
    ingredients = @(
        @{ productId = $sofaId; quantity = 1; measurementUnit = "Unit" },
        @{ productId = $coffeeTableId; quantity = 1; measurementUnit = "Unit" }
    )
}
Write-Host "Seeding Formula: Cozy Living"
$formula2Id = $null
try {
    $resp2 = Invoke-RestMethod -Uri 'http://localhost:27001/api/suite-formulas' -Method Post -Headers $headers -Body ($formula2 | ConvertTo-Json -Depth 10)
    $formula2Id = $resp2.id
    Write-Host "Created Formula: $formula2Id"
} catch {
    if ($_.Exception.Response.StatusCode -eq "Conflict") {
        Write-Host "Formula already exists. Fetching ID..."
        $formulas = Invoke-RestMethod -Uri 'http://localhost:27001/api/suite-formulas' -Method Get -Headers $headers
        $formula2Id = ($formulas | Where-Object { $_.familyId -eq $homeId })[0].id
        Write-Host "Found Existing Formula ID: $formula2Id"
    } else {
        Write-Error "Failed to seed Formula 2"
    }
}

# --- Suite 2 Instance ---
if ($formula2Id) {
    $suite2 = @{
        name = "Urban Living Bundle"
        description = "Perfect combination for small apartments."
        formulaId = $formula2Id
        familyId = $homeId
        remark = "Urban Collection"
    }
    Write-Host "Seeding Suite: Urban Living Bundle"
    try {
        Invoke-RestMethod -Uri 'http://localhost:27001/api/suites' -Method Post -Headers $headers -Body ($suite2 | ConvertTo-Json)
        Write-Host "Successfully seeded Suite 2"
    } catch {
        Write-Host "Suite 2 already exists or failed."
    }
}


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
$formula3Id = $null
try {
    $resp3 = Invoke-RestMethod -Uri 'http://localhost:27001/api/suite-formulas' -Method Post -Headers $headers -Body ($formula3 | ConvertTo-Json -Depth 10)
    $formula3Id = $resp3.id
    Write-Host "Created Formula: $formula3Id"
} catch {
    if ($_.Exception.Response.StatusCode -eq "Conflict") {
         Write-Host "Formula already exists. Fetching ID..."
        $formulas = Invoke-RestMethod -Uri 'http://localhost:27001/api/suite-formulas' -Method Get -Headers $headers
        $formula3Id = ($formulas | Where-Object { $_.familyId -eq $hardwareId })[0].id
        Write-Host "Found Existing Formula ID: $formula3Id"
    } else {
        Write-Error "Failed to seed Formula 3"
    }
}

# --- Suite 3 Instance ---
if ($formula3Id) {
    $suite3 = @{
        name = "Full Kitchen Hardware Pack"
        description = "Hardware kit for standard kitchen cabinetry."
        formulaId = $formula3Id
        familyId = $hardwareId
        remark = "Polished Collection"
    }
    Write-Host "Seeding Suite: Full Kitchen Hardware Pack"
    try {
        Invoke-RestMethod -Uri 'http://localhost:27001/api/suites' -Method Post -Headers $headers -Body ($suite3 | ConvertTo-Json)
        Write-Host "Successfully seeded Suite 3"
    } catch {
        Write-Host "Suite 3 already exists or failed."
    }
}
