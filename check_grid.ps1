param (
    [string]$HubUrl = "http://localhost:4444/status"
)

try {
    $response = Invoke-RestMethod -Uri $HubUrl -UseBasicParsing -TimeoutSec 3
    if ($response.value.ready) {
        # Grid is ready
        exit 0
    } else {
        # Grid not ready
        exit 2
    }
}
catch {
    # No connection or different problem
    exit 3
}