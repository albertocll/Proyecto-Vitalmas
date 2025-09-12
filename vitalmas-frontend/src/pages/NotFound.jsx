import { Link } from "react-router-dom";

export default function NotFound() {
  return (
    <main className="mx-auto max-w-2xl px-4 py-16 text-center">
      <h1 className="text-5xl font-semibold tracking-tight text-slate-100">404</h1>
      <p className="mt-4 text-lg text-slate-300">Página no encontrada</p>
      <p className="mt-2 text-slate-400">
        La ruta solicitada no existe o ha sido movida.
      </p>

      <div className="mt-8">
        <Link
          to="/"
          className="inline-flex items-center justify-center rounded-xl px-4 py-2 text-sm font-medium
                     bg-cyan-500/90 text-slate-900 hover:bg-cyan-400
                     focus:outline-none focus-visible:ring-2 focus-visible:ring-cyan-400
                     focus-visible:ring-offset-2 focus-visible:ring-offset-slate-900"
        >
          Ir al inicio
        </Link>
      </div>
    </main>
  );
}
