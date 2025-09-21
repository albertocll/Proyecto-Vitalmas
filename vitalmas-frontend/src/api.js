const BASE = import.meta.env.VITE_API_BASE || "/api"

// credenciales básicas codificadas en Base64
const AUTH = "Basic " + btoa("house:house123")

export async function listarEnfermedades(q) {
  const url = q
    ? `${BASE}/enfermedades?q=${encodeURIComponent(q)}`
    : `${BASE}/enfermedades`
  const res = await fetch(url, {
    headers: { Authorization: AUTH }
  })
  if (!res.ok) throw new Error("Error listando enfermedades")
  return res.json()
}

export async function getEnfermedad(nombre) {
  const res = await fetch(`${BASE}/enfermedades/${encodeURIComponent(nombre)}`, {
    headers: { Authorization: AUTH }
  })
  if (res.status === 404) return null
  if (!res.ok) throw new Error("Error obteniendo enfermedad")
  return res.json()
}

export async function listarSintomas(nombre) {
  const res = await fetch(`${BASE}/enfermedades/${encodeURIComponent(nombre)}/sintomas`, {
    headers: { Authorization: AUTH }
  })
  if (!res.ok) throw new Error("Error listando síntomas")
  return res.json()
}

// esta función ya no inventa un endpoint, simplemente comprueba credenciales
export async function loginBasic(usuario, password) {
  const token = "Basic " + btoa(`${usuario}:${password}`)
  const res = await fetch(`${BASE}/enfermedades`, {
    headers: { Authorization: token }
  })
  if (res.status === 401) return null
  if (!res.ok) throw new Error("Error en login")
  return true // devolvemos true si las credenciales sirven
}
