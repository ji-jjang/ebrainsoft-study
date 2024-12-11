import { defineConfig } from "vite";
import react from "@vitejs/plugin-react-swc";
import terser from "@rollup/plugin-terser";

export default defineConfig({
  plugins: [react()],

  build: {
    rollupOptions: {
      plugins: [
        terser({
          format: {
            comments: false,
          },
          compress: {
            drop_console: true,
            drop_debugger: true,
          },
        }),
      ],
    },
  },
});
