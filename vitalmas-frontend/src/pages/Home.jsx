import { useEffect, useMemo, useState } from 'react'
import { listarEnfermedades } from '../api'
import { Link, useLocation, useSearchParams } from 'react-router-dom'
import RiskBadge from '../components/RiskBadge'

export default function Home() {
  const [data, setData] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  const [searchParams, setSearchParams] = useSearchParams()
  const [q, setQ] = useState(() => searchParams.get('q') ?? '')
  const location = useLocation()

  useEffect(() => {
    const currentQ = searchParams.get('q') ?? ''
    if (currentQ !== q) setQ(currentQ)
  }, [searchParams]) // eslint-disable-line react-hooks/exhaustive-deps

  useEffect(() => {
    const currentQ = searchParams.get('q') ?? ''
    if (q === '') {
      if (currentQ !== '') setSearchParams({}, { replace: true })
    } else if (q !== currentQ) {
      setSearchParams({ q }, { replace: true })
    }
  }, [q, searchParams, setSearchParams])

  useEffect(() => {
    listarEnfermedades()
      .then(setData)
      .catch(e => setError(e.message))
      .finally(() => setLoading(false))
  }, [])

  const filtered = useMemo(() => {
    if (!q) return data
    const s = q.toLowerCase()
    return data.filter(e => e.nombre.toLowerCase().includes(s))
  }, [data, q])

  if (loading) {
    return (
      <div className="mx-auto mt-8 grid max-w-7xl grid-cols-1 gap-3 px-4 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5">
        {Array.from({ length: 8 }).map((_, i) => (
          <div key={i} className="h-24 animate-pulse rounded-2xl border border-slate-800 bg-slate-900/60" />
        ))}
      </div>
    )
  }

  if (error) {
    return (
      <div className="mx-auto mt-16 max-w-7xl px-4">
        <div className="rounded-lg border border-red-900/40 bg-red-950/70 px-4 py-3 text-red-200 shadow">
          <strong className="font-semibold">Error:</strong> {error}
        </div>
      </div>
    )
  }

  return (
    <div className="mx-auto max-w-7xl px-4 pt-8">
      <div className="mb-6 flex flex-col gap-3 sm:flex-row sm:items-end sm:justify-between">
        <h2 className="flex items-center gap-2 text-2xl font-semibold">
          Enfermedades
          <span
            className="inline-flex items-center justify-center rounded-full border border-slate-700 bg-slate-900/60 px-2 py-0.5 text-xs text-slate-300"
            aria-live="polite"
          >
            {filtered.length}
          </span>
        </h2>
        <div className="w-full sm:w-80">
          <input
            value={q}
            onChange={(e) => setQ(e.target.value)}
            placeholder="Buscar por nombre…"
            className="h-10 w-full rounded-lg border border-slate-800 bg-slate-900 px-3 text-sm outline-none focus:ring-2 focus:ring-cyan-400"
          />
        </div>
      </div>

      {/* Grid de resultados */}
      <ul className="grid grid-cols-1 gap-3 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5">
        {filtered.slice(0, 100).map(e => (
          <li
            key={e.nombre}
            className="min-w-0 rounded-2xl border border-slate-800 bg-slate-900/60 p-4 shadow-sm transition hover:-translate-y-0.5 hover:border-slate-700 hover:shadow-md h-full"
          >
            <div className="h-full flex flex-col">
              <Link
                to={`/enfermedad/${encodeURIComponent(e.nombre)}`}
                state={{ from: location }}
                className="block break-words text-cyan-300 hover:underline"
              >
                {e.nombre}
              </Link>
              <div className="mt-auto">
                <RiskBadge nivel={e.nivelRiesgo} />
              </div>
            </div>
          </li>
        ))}
      </ul>

      {filtered.length === 0 && (
        <div className="mt-10 flex flex-col items-center justify-center rounded-2xl border border-slate-800 bg-slate-900/60 p-8 text-center">
          <p className="text-slate-300">
            No se encontraron resultados para <span className="font-semibold text-slate-200">“{q}”</span>.
          </p>
          <button
            onClick={() => setQ('')}
            className="mt-4 inline-flex h-9 items-center rounded-lg border border-slate-700 px-3 text-sm hover:bg-slate-800 focus-visible:ring-2 focus-visible:ring-cyan-400"
          >
            Limpiar búsqueda
          </button>
        </div>
      )}

      {data.length > 100 && filtered.length >= 100 && (
        <p className="mt-6 text-sm text-slate-400">
          Mostrando 100 de {filtered.length}. Ajusta la búsqueda para filtrar.
        </p>
      )}
    </div>
  )
}
