import { Link } from 'react-router-dom'

export default function Inicio() {
  return (
    <div className="mx-auto max-w-7xl px-4 py-10">
      {/* Hero centrado */}
      <section className="rounded-3xl border border-slate-800 bg-slate-900/60 p-8 md:p-10 backdrop-blur">
        <div className="mx-auto max-w-3xl text-center">
          <img
            src="/brand/vitalmas-logotype.png"
            alt="Vitalmas"
            className="mx-auto mb-5 h-16 md:h-20 select-none"
            loading="eager"
          />
          <h1 className="sr-only">Vitalmas</h1>

          <p className="mt-1 text-lg md:text-xl text-slate-200">
            Consulta y gestión básica de enfermedades. Rápido, sobrio y sin humo.
          </p>

          <div className="mt-6 flex flex-wrap justify-center gap-3">
            <Link
              to="/login"
              className="inline-flex h-10 items-center rounded-lg bg-cyan-500 px-4 font-semibold text-slate-950 hover:brightness-110 focus-visible:ring-2 focus-visible:ring-cyan-400"
            >
              Ingresar
            </Link>
            <Link
              to="/"
              className="inline-flex h-10 items-center rounded-lg border border-slate-700 px-4 hover:bg-slate-800 focus-visible:ring-2 focus-visible:ring-cyan-400"
            >
              Ver enfermedades
            </Link>
          </div>
        </div>
      </section>

      {/* Atajos (centrado en anchas) */}
      <section className="mt-8 grid max-w-5xl grid-cols-1 gap-3 sm:grid-cols-2 lg:grid-cols-3 mx-auto">
        <Link
          to="/"
          className="h-full rounded-2xl border border-slate-800 bg-slate-900/60 p-5 transition hover:-translate-y-0.5 hover:border-slate-700"
        >
          <div className="h-full flex flex-col">
            <h3 className="text-cyan-300">Listado</h3>
            <p className="mt-auto text-sm text-slate-400">
              Explora enfermedades y su nivel de riesgo.
            </p>
          </div>
        </Link>

        <Link
          to="/login"
          className="h-full rounded-2xl border border-slate-800 bg-slate-900/60 p-5 transition hover:-translate-y-0.5 hover:border-slate-700"
        >
          <div className="h-full flex flex-col">
            <h3 className="text-cyan-300">Acceso</h3>
            <p className="mt-auto text-sm text-slate-400">
              Inicio de sesión para médicos.
            </p>
          </div>
        </Link>

        <div className="h-full rounded-2xl border border-slate-800 bg-slate-900/60 p-5">
          <div className="h-full flex flex-col">
            <h3 className="text-cyan-300">Tecnología</h3>
            <p className="mt-auto text-sm text-slate-400">
              Backend Spring Boot; Frontend React + Vite + Tailwind. Interfaz rápida y ligera.
            </p>
          </div>
        </div>
      </section>
    </div>
  )
}
