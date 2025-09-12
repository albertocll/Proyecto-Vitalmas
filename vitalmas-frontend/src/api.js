const BASE = import.meta.env.VITE_API_BASE || '/api'

export async function listarEnfermedades(q) {
  const url = q ? `${BASE}/enfermedades?q=${encodeURIComponent(q)}` : `${BASE}/enfermedades`
  const res = await fetch(url)
  if (!res.ok) throw new Error('Error listando enfermedades')
  return res.json()
}

export async function getEnfermedad(nombre) {
  const res = await fetch(`${BASE}/enfermedades/${encodeURIComponent(nombre)}`)
  if (res.status === 404) return null
  if (!res.ok) throw new Error('Error obteniendo enfermedad')
  return res.json()
}

export async function listarSintomas(nombre) {
  const res = await fetch(`${BASE}/enfermedades/${encodeURIComponent(nombre)}/sintomas`)
  if (!res.ok) throw new Error('Error listando síntomas')
  return res.json()
}

export async function loginBasic(usuario, password) {
  const BASE = import.meta.env.VITE_API_BASE || '/api'
  const res = await fetch(`${BASE}/login/basic`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ usuario, password })
  })
  if (res.status === 401) return null
  if (!res.ok) throw new Error('Error en login')
  return res.json()
}
