import nextCoreWebVitals from "eslint-config-next/core-web-vitals";
import nextTypeScript from "eslint-config-next/typescript";

const eslintConfig = [
  ...nextCoreWebVitals,
  ...nextTypeScript,
  {
    rules: {
      // The app uses try/catch around async server-component data loading.
      "react-hooks/error-boundaries": "off"
    }
  },
  {
    ignores: [
      ".next/**",
      "coverage/**",
      "dist/**",
      "build/**",
      "next-env.d.ts",
      "*.min.js"
    ]
  }
];

export default eslintConfig;
