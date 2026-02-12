$headers = @{
    'Content-Type'  = 'application/json'
    'Authorization' = 'Bearer <JWT_TOKEN_PLACEHOLDER>'
}

$customers = @(
    @{ name = "Global Office Hub"; description = "International supplier of premium office furniture."; address = "123 Business Way, New York, NY" },
    @{ name = "Modern Living Retail"; description = "High-end home furniture retail chain."; address = "456 Design St, San Francisco, CA" },
    @{ name = "Precision Engineering Corp"; description = "Industrial manufacturing firm requiring high-durability hardware."; address = "789 Factory Rd, Chicago, IL" },
    @{ name = "Comfort Suites Hotel"; description = "Boutique hotel chain focusing on guest experience and aesthetics."; address = "101 Luxury Ave, Miami, FL" },
    @{ name = "Urban Workspace Solutions"; description = "Co-working space provider with modern, flexible furniture needs."; address = "202 Collaboration Blvd, Austin, TX" }
)

$customerIds = @{}

foreach ($c in $customers) {
    Write-Host "Seeding customer: $($c.name)"
    $body = $c | ConvertTo-Json
    $response = Invoke-RestMethod -Uri 'http://localhost:27001/api/customers' -Method Post -Headers $headers -Body $body
    $customerIds[$c.name] = $response.id
    Write-Host "Created customer $($c.name) with ID $($response.id)"
}

$segments = @(
    @{ 
        name          = "Premium Retail"
        description   = "High-end retail partners focusing on home and lifestyle furniture."
        customerNames = @("Modern Living Retail")
    },
    @{ 
        name          = "Corporate Enterprise"
        description   = "Large-scale corporate clients for office and industrial solutions."
        customerNames = @("Global Office Hub", "Precision Engineering Corp", "Urban Workspace Solutions")
    },
    @{ 
        name          = "Hospitality"
        description   = "Hotels, resorts, and restaurants with consistent aesthetic requirements."
        customerNames = @("Comfort Suites Hotel")
    }
)

foreach ($s in $segments) {
    Write-Host "Seeding segment: $($s.name)"
    $ids = @()
    foreach ($name in $s.customerNames) {
        $ids += $customerIds[$name]
    }
    $body = @{
        name        = $s.name
        description = $s.description
        customerIds = $ids
    } | ConvertTo-Json
    $response = Invoke-RestMethod -Uri 'http://localhost:27001/api/customers/segments' -Method Post -Headers $headers -Body $body
    Write-Host "Created segment $($s.name) with ID $($response.id)"
}
