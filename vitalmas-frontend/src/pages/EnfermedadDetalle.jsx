import { useEffect, useMemo, useState } from 'react'
import { useParams, useNavigate, useLocation } from 'react-router-dom'
import { listarEnfermedades } from '../api'
import RiskBadge from '../components/RiskBadge'

export default function EnfermedadDetalle() {
  const { nombre } = useParams()
  const decoded = useMemo(() => {
    try {
      return decodeURIComponent(nombre || '')
    } catch {
      return nombre || ''
    }
  }, [nombre])

  const [item, setItem] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  const nav = useNavigate()
  const location = useLocation()

  useEffect(() => {
    setLoading(true)
    listarEnfermedades()
      .then(list => {
        const found = list.find(
          e => (e?.nombre || '').toLowerCase() === decoded.toLowerCase()
        )
        setItem(found || null)
      })
      .catch(e => setError(e?.message || 'Error al cargar'))
      .finally(() => setLoading(false))
  }, [decoded])

  // Loading skeleton
  if (loading) {
    return (
      <div className="mx-auto mt-8 max-w-3xl px-4">
        <div className="h-28 animate-pulse rounded-2xl border border-slate-800 bg-slate-900/60" />
      </div>
    )
  }

  // Error
  if (error) {
    return (
      <div className="mx-auto mt-16 max-w-3xl px-4">
        <div className="rounded-2xl border border-red-900/40 bg-red-950/70 p-6 text-red-200">
          <p className="font-semibold">Error</p>
          <p className="mt-1 text-sm">{error}</p>
          <button
            onClick={() => nav(-1)}
            className="mt-4 inline-flex h-9 items-center rounded-lg border border-slate-700 px-3 text-sm text-slate-200 hover:bg-slate-800 focus-visible:ring-2 focus-visible:ring-cyan-400"
          >
            Volver
          </button>
        </div>
      </div>
    )
  }

  // Not found
  if (!item) {
    return (
      <div className="mx-auto mt-16 max-w-3xl px-4">
        <div className="rounded-2xl border border-slate-800 bg-slate-900/60 p-6">
          <p className="text-slate-300">
            No se encontró ninguna enfermedad con el nombre{' '}
            <span className="font-semibold text-slate-200">“{decoded}”</span>.
          </p>
          <button
            onClick={() => nav('/')}
            className="mt-4 inline-flex h-9 items-center rounded-lg border border-slate-700 px-3 text-sm hover:bg-slate-800 focus-visible:ring-2 focus-visible:ring-cyan-400"
          >
            Volver al listado
          </button>
        </div>
      </div>
    )
  }

  // Detail view
  return (
    <div className="mx-auto mt-8 max-w-3xl px-4">
      <div className="rounded-2xl border border-slate-800 bg-slate-900/60 p-6">
        <div className="flex flex-wrap items-center justify-between gap-3">
          <h1 className="text-2xl font-semibold text-slate-100">{item.nombre}</h1>
          <RiskBadge nivel={item.nivelRiesgo} />
        </div>

        <div className="mt-4 grid gap-3 sm:grid-cols-2">
          <div className="rounded-xl border border-slate-800/70 bg-slate-950/40 p-4">
            <p className="text-sm text-slate-400">Nivel de riesgo</p>
            <p className="mt-1 font-medium text-slate-200">
              {item.nivelRiesgo || '—'}
            </p>
          </div>

          <div className="rounded-xl border border-slate-800/70 bg-slate-950/40 p-4">
            <p className="text-sm text-slate-400">Código</p>
            <p className="mt-1 font-medium text-slate-200">{item.codigo || '—'}</p>
          </div>
        </div>

        <div className="mt-4">
          <p className="text-sm text-slate-400">Descripción</p>
          <p className="mt-1 text-slate-300">
            {item.descripcion || 'Descripción no disponible.'}
          </p>
        </div>

        <div className="mt-6">
          <button
            onClick={() => {
              const from = location.state?.from
              if (from) {
                nav(
                  from.pathname + (from.search || '') + (from.hash || '')
                )
              } else {
                nav(-1)
              }
            }}
            className="inline-flex h-9 items-center rounded-lg border border-slate-700 px-3 text-sm hover:bg-slate-800 focus-visible:ring-2 focus-visible:ring-cyan-400"
          >
            Volver
          </button>
        </div>
      </div>
    </div>
  )
}
