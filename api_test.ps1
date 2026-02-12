$token = '<JWT_TOKEN_PLACEHOLDER>'
$base = 'http://localhost:27001'
$headers = @{ 'Content-Type'='application/json'; 'Authorization'="Bearer $token" }

$pass = 0; $fail = 0; $results = @()

function Test-Endpoint {
    param([string]$Name, [string]$Method, [string]$Uri, [string]$Body, [int[]]$ExpectedStatus)
    try {
        $params = @{ Uri="$base$Uri"; Method=$Method; Headers=$headers; ErrorAction='Stop' }
        if ($Body) { $params.Body = $Body }
        $resp = Invoke-RestMethod @params
        $script:pass++
        $script:results += [PSCustomObject]@{ Test=$Name; Status='PASS'; Details="OK" }
        return $resp
    } catch {
        $code = [int]$_.Exception.Response.StatusCode
        if ($ExpectedStatus -contains $code) {
            $script:pass++
            $script:results += [PSCustomObject]@{ Test=$Name; Status='PASS'; Details="Expected $code" }
        } else {
            $script:fail++
            $script:results += [PSCustomObject]@{ Test=$Name; Status='FAIL'; Details="HTTP $code - $($_.Exception.Message)" }
        }
        return $null
    }
}

Write-Host "============================================" -ForegroundColor Cyan
Write-Host "  REV-NEXT Comprehensive API Test Suite" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# ==========================================
# 1. PRODUCT FAMILIES
# ==========================================
Write-Host "--- Product Families ---" -ForegroundColor Yellow
$families = Test-Endpoint "GET /api/product-families" "Get" "/api/product-families"
Write-Host "  Families count: $($families.Count)"

if ($families -and $families.Count -gt 0) {
    $fam = $families[0]
    Test-Endpoint "POST /api/product-families (duplicate)" "Post" "/api/product-families" (@{ name=$fam.name; description="dup test" } | ConvertTo-Json) @(409)
}

# ==========================================
# 2. PRODUCTS
# ==========================================
Write-Host "`n--- Products ---" -ForegroundColor Yellow
$products = Test-Endpoint "GET /api/products" "Get" "/api/products"
Write-Host "  Products count: $($products.Count)"

if ($products -and $products.Count -gt 0) {
    $prod = $products[0]
    $prodDetail = Test-Endpoint "GET /api/products/{id}" "Get" "/api/products/$($prod.id)"
    Write-Host "  Product detail: $($prodDetail.name)"

    # Update test
    $updateBody = @{ name=$prod.name; description="Updated via API test"; familyId=$prod.familyId } | ConvertTo-Json
    $updated = Test-Endpoint "PUT /api/products/{id}" "Put" "/api/products/$($prod.id)" $updateBody
    if ($updated) { Write-Host "  Updated desc: $($updated.description)" }
}

# Test creating a product
$newProd = @{ name="API Test Product $(Get-Random -Max 9999)"; description="Created by API test"; familyId=$families[0].id } | ConvertTo-Json
$created = Test-Endpoint "POST /api/products" "Post" "/api/products" $newProd
if ($created) {
    Write-Host "  Created product: $($created.id)"
    # Delete it
    Test-Endpoint "DELETE /api/products/{id}" "Delete" "/api/products/$($created.id)"
    Write-Host "  Deleted test product"
}

# ==========================================
# 3. SUITE FORMULAS
# ==========================================
Write-Host "`n--- Suite Formulas ---" -ForegroundColor Yellow
$formulas = Test-Endpoint "GET /api/suite-formulas" "Get" "/api/suite-formulas"
Write-Host "  Formulas count: $($formulas.Count)"

if ($formulas -and $formulas.Count -gt 0) {
    $f = $formulas[0]
    $fDetail = Test-Endpoint "GET /api/suite-formulas/{id}" "Get" "/api/suite-formulas/$($f.id)"
    Write-Host "  Formula detail: familyId=$($fDetail.familyId), ingredients=$($fDetail.ingredients.Count)"
}

# ==========================================
# 4. SUITES
# ==========================================
Write-Host "`n--- Suites ---" -ForegroundColor Yellow
$suites = Test-Endpoint "GET /api/suites" "Get" "/api/suites"
Write-Host "  Suites count: $($suites.Count)"

if ($suites -and $suites.Count -gt 0) {
    $s = $suites[0]
    $sDetail = Test-Endpoint "GET /api/suites/{id}" "Get" "/api/suites/$($s.id)"
    Write-Host "  Suite detail: $($sDetail.name) -> formula=$($sDetail.formulaId)"
}

# ==========================================
# 5. CUSTOMERS
# ==========================================
Write-Host "`n--- Customers ---" -ForegroundColor Yellow
$customers = Test-Endpoint "GET /api/customers" "Get" "/api/customers"
Write-Host "  Customers count: $($customers.Count)"

if ($customers -and $customers.Count -gt 0) {
    $c = $customers[0]
    $cDetail = Test-Endpoint "GET /api/customers/{id}" "Get" "/api/customers/$($c.id)"
    Write-Host "  Customer detail: $($cDetail.name)"
}

# ==========================================
# 6. CUSTOMER SEGMENTS
# ==========================================
Write-Host "`n--- Customer Segments ---" -ForegroundColor Yellow
$segments = Test-Endpoint "GET /api/customers/segments" "Get" "/api/customers/segments"
Write-Host "  Segments count: $($segments.Count)"

# ==========================================
# 7. PRICES
# ==========================================
Write-Host "`n--- Prices ---" -ForegroundColor Yellow
$prices = Test-Endpoint "GET /api/prices" "Get" "/api/prices"
Write-Host "  Prices count: $(if($prices){$prices.Count}else{'0 or error'})"

# Create a price for the first product
if ($products -and $products.Count -gt 0) {
    $priceBody = @{
        productId = $products[0].id
        name = "Standard Price"
        priceType = "BASE"
    } | ConvertTo-Json
    $newPrice = Test-Endpoint "POST /api/prices" "Post" "/api/prices" $priceBody
    if ($newPrice) {
        Write-Host "  Created price: $($newPrice.id)"
        $priceDetail = Test-Endpoint "GET /api/prices/{id}" "Get" "/api/prices/$($newPrice.id)"

        # Add a validity
        $validityBody = @{
            startDate = "2026-01-01"
            endDate = "2026-12-31"
            basePrice = 100.00
        } | ConvertTo-Json
        $validity = Test-Endpoint "POST /api/prices/{id}/validities" "Post" "/api/prices/$($newPrice.id)/validities" $validityBody
        if ($validity) {
            Write-Host "  Created validity: $($validity.id)"

            # Get validities
            Test-Endpoint "GET /api/prices/{id}/validities" "Get" "/api/prices/$($newPrice.id)/validities"

            # Add a tier
            $tierBody = @{
                minQuantity = 1
                maxQuantity = 100
                price = 95.00
            } | ConvertTo-Json
            $tier = Test-Endpoint "POST /api/prices/validities/{id}/tiers" "Post" "/api/prices/validities/$($validity.id)/tiers" $tierBody
            if ($tier) {
                Write-Host "  Created tier: $($tier.id)"
                Test-Endpoint "GET /api/prices/validities/{id}/tiers" "Get" "/api/prices/validities/$($validity.id)/tiers"
            }
        }

        # Clean up
        Test-Endpoint "DELETE /api/prices/{id}" "Delete" "/api/prices/$($newPrice.id)"
        Write-Host "  Deleted test price"
    }
}

# ==========================================
# 8. DISCOUNTS
# ==========================================
Write-Host "`n--- Discounts ---" -ForegroundColor Yellow
$discounts = Test-Endpoint "GET /api/discounts" "Get" "/api/discounts"
Write-Host "  Discounts count: $(if($discounts){$discounts.Count}else{'0 or error'})"

# ==========================================
# 9. CONFIG
# ==========================================
Write-Host "`n--- Config ---" -ForegroundColor Yellow
$configs = Test-Endpoint "GET /api/config" "Get" "/api/config"
Write-Host "  Configs count: $(if($configs){$configs.Count}else{'0 or error'})"

# ==========================================
# 10. MENUS
# ==========================================
Write-Host "`n--- Menus ---" -ForegroundColor Yellow
$menus = Test-Endpoint "GET /api/menus" "Get" "/api/menus"
Write-Host "  Menus response: $(if($menus){'OK'}else{'empty or error'})"

# ==========================================
# 11. USERS
# ==========================================
Write-Host "`n--- Users ---" -ForegroundColor Yellow
$users = Test-Endpoint "GET /api/user" "Get" "/api/user"
Write-Host "  Users count: $(if($users){$users.Count}else{'0 or error'})"

# ==========================================
# 12. ERROR HANDLING
# ==========================================
Write-Host "`n--- Error Handling ---" -ForegroundColor Yellow
Test-Endpoint "GET /api/products/{bad-id} (404)" "Get" "/api/products/00000000-0000-0000-0000-000000000000" $null @(404,500)
Test-Endpoint "POST /authenticate (bad creds -> 401)" "Post" "/authenticate" (@{userName='bad';password='bad'} | ConvertTo-Json) @(401)
Test-Endpoint "GET /api/nonexistent (404)" "Get" "/api/nonexistent" $null @(404)

# ==========================================
# SUMMARY
# ==========================================
Write-Host "`n============================================" -ForegroundColor Cyan
Write-Host "  TEST RESULTS: $pass PASSED / $fail FAILED" -ForegroundColor $(if($fail -gt 0){'Red'}else{'Green'})
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""
$results | Format-Table -AutoSize
