import { useState } from "react";
import { loginBasic } from "../api";
import { useNavigate, useLocation } from "react-router-dom";
import Button from "../components/ui/Button.jsx";
import TextInput from "../components/ui/TextInput.jsx";

export default function Login() {
  const [usuario, setUsuario] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const nav = useNavigate();
  const location = useLocation();

  const onSubmit = async (e) => {
    e.preventDefault();
    if (loading) return;
    setError("");
    setLoading(true);
    try {
      const data = await loginBasic(usuario, password);
      if (!data) {
        setError("Credenciales inválidas");
        return;
      }
      const safeAuth = {
        tipo: data.tipo,
        usuario: {
          nombre: data.usuario?.nombre,
          usuario: data.usuario?.usuario,
        },
      };
      sessionStorage.setItem("auth", JSON.stringify(safeAuth));

      const from = location.state?.from;
      const to = from
        ? `${from.pathname}${from.search || ""}${from.hash || ""}`
        : "/";
      nav(to, { replace: true });
    } catch (err) {
      setError(err?.message || "Error al iniciar sesión");
    } finally {
      setLoading(false);
    }
  };

  const errorId = "login-error";

  return (
    <div className="mt-16 grid w-full place-items-center px-4">
      <div className="mx-auto w-full max-w-md rounded-2xl border border-slate-800 bg-slate-900/60 p-6 backdrop-blur">
        <h2 className="mb-2 text-xl font-semibold">Ingresar (Médico)</h2>
        <p className="mb-4 text-sm text-slate-400">
          Prueba: <code>house / house123</code>
        </p>

        <form
          onSubmit={onSubmit}
          className="space-y-4"
          autoComplete="on"
          noValidate
        >
          <TextInput
            id="usuario"
            label="Usuario"
            name="username"
            autoComplete="username"
            autoCapitalize="none"
            autoCorrect="off"
            spellCheck="false"
            inputMode="text"
            required
            placeholder="Usuario"
            value={usuario}
            onChange={(e) => setUsuario(e.target.value)}
            error={error}
            disabled={loading}
          />

          <TextInput
            id="password"
            label="Contraseña"
            name="password"
            type="password"
            autoComplete="current-password"
            required
            placeholder="Contraseña"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            error={error}
            disabled={loading}
          />

          <div className="flex gap-2 pt-2">
            <Button
              type="submit"
              variant="primary"
              disabled={loading}
              className="h-10 font-semibold disabled:cursor-not-allowed disabled:opacity-60"
            >
              {loading && (
                <span className="mr-2 inline-block h-4 w-4 animate-spin rounded-full border-2 border-slate-900 border-t-transparent" />
              )}
              Entrar
            </Button>
            <Button
              type="button"
              variant="outline"
              disabled={loading}
              className="h-10 disabled:cursor-not-allowed disabled:opacity-60"
              onClick={() => {
                setUsuario("");
                setPassword("");
                setError("");
              }}
            >
              Limpiar
            </Button>
          </div>
        </form>

        {error && (
          <p
            id={errorId}
            role="alert"
            aria-live="polite"
            className="mt-3 text-sm text-red-400"
          >
            {error}
          </p>
        )}
      </div>
    </div>
  );
}
