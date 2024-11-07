# istio-retry-simulation
Here’s a sample `README.md` file for a GitHub repository containing the Istio `VirtualService` configuration with retry logic.

---

# Istio VirtualService with Retry Configuration

This repository demonstrates how to configure an Istio `VirtualService` with retry logic for handling network failures. This configuration allows requests to retry upon failure with a specified number of attempts and a timeout duration for each attempt.

## Prerequisites

- Kubernetes cluster with [Istio](https://istio.io/latest/docs/setup/getting-started/) installed.
- A service (`service-1-retry`) deployed in the `sumanns` namespace, exposing the `/hello` endpoint.

## Configuration Files

### 1. `service-1-virtualservice-retry.yaml`

This file contains the Istio `VirtualService` configuration for `service-1-retry` with a retry policy.

```yaml
apiVersion: networking.istio.io/v1alpha3
kind: VirtualService
metadata:
  name: service-1-virtualservice-retry
  namespace: sumanns
spec:
  hosts:
  - "*"
  gateways:
  - service-1-gateway-retry
  http:
  - match:
      - uri:
          exact: /hello
    retries:
      attempts: 3              # Number of retry attempts
      perTryTimeout: 7s         # Timeout per retry attempt
    route:
    - destination:
        host: service-1-retry.sumanns.svc.cluster.local
        subset: v1
        port:
          number: 80
      weight: 100
```

### Explanation

- **`retries`**: This section defines the retry behavior.
  - `attempts`: The number of times Istio should retry a failed request.
  - `perTryTimeout`: The maximum duration allowed for each retry attempt.

- **Route Configuration**: 
  - **`host`**: Specifies the fully qualified domain name (FQDN) of the target service (`service-1-retry.sumanns.svc.cluster.local`).
  - **`subset`**: Defines the subset (version) of the service to route to. In this example, we're routing to subset `v1`.
  - **`port`**: The port on which the target service is listening.

## Usage

1. **Apply the Namespace**:
   Make sure the `sumanns` namespace is created in your cluster:

   ```bash
   kubectl create namespace sumanns
   ```

2. **Deploy the Service and Gateway**:
   Ensure the target service (`service-1-retry`) and the Istio Gateway (`service-1-gateway-retry`) are deployed in the `sumanns` namespace.

3. **Apply the VirtualService Configuration**:
   Use the following command to apply the `VirtualService` configuration:

   ```bash
   kubectl apply -f service-1-virtualservice-retry.yaml
   ```

4. **Test the Configuration**:
   Access the `/hello` endpoint through the Istio Ingress Gateway’s external IP. You can get the IP address with:

   ```bash
   kubectl get svc istio-ingressgateway -n istio-system
   ```

   Example request:

   ```bash
   curl http://<EXTERNAL_IP>/hello
   ```

   Istio will automatically retry up to 3 times if there are network issues, with a 7-second timeout for each retry attempt.

## Additional Notes

- **Timeouts**: Adjust `perTryTimeout` and `attempts` as needed based on your service’s tolerance for retries and latency.
- **Subset Configuration**: Ensure that the subset `v1` is defined in the `DestinationRule` for `service-1-retry` with the appropriate labels.
