const DEFAULT_USERNAME = "operador";
const DEFAULT_PASSWORD = "operador123";

export function getBasicAuthHeader() {
  const username =
    process.env.NEXT_PUBLIC_FIDC_API_USERNAME ?? DEFAULT_USERNAME;
  const password =
    process.env.NEXT_PUBLIC_FIDC_API_PASSWORD ?? DEFAULT_PASSWORD;
  const token = Buffer.from(`${username}:${password}`).toString("base64");
  return `Basic ${token}`;
}
