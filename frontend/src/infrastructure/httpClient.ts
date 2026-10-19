export class ApiError extends Error {
  readonly status: number

  constructor(status: number, message: string) {
    super(message)
    this.status = status
  }
}

export async function request<T>(url: string, init: RequestInit = {}): Promise<T> {
  const response = await fetch(url, {
    ...init,
    headers: { 'Content-Type': 'application/json', ...init.headers },
  })
  if (!response.ok) {
    throw new ApiError(response.status, await errorMessage(response))
  }
  return response.status === 204 ? (undefined as T) : ((await response.json()) as T)
}

async function errorMessage(response: Response): Promise<string> {
  try {
    const problem = (await response.json()) as { detail?: string }
    return problem.detail ?? `Erro ${response.status}`
  } catch {
    return `Erro ${response.status}`
  }
}
