import { describe, expect, it } from 'vitest'
import { availableActions, validateTaskInput } from '../domain/task'
import { toQueryString } from '../infrastructure/httpTaskRepository'

describe('availableActions', () => {
  it('offers only valid transitions for each status', () => {
    expect(availableActions('TODO')).toEqual(['start', 'complete'])
    expect(availableActions('IN_PROGRESS')).toEqual(['complete', 'reopen'])
    expect(availableActions('DONE')).toEqual(['reopen'])
  })
})

describe('validateTaskInput', () => {
  const base = { description: '', priority: 'LOW' as const, dueDate: null }

  it('rejects blank titles', () => {
    expect(validateTaskInput({ ...base, title: '   ' })).toBe('O título é obrigatório.')
  })

  it('rejects titles over 120 characters', () => {
    expect(validateTaskInput({ ...base, title: 'x'.repeat(121) })).not.toBeNull()
  })

  it('accepts a valid title', () => {
    expect(validateTaskInput({ ...base, title: 'Estudar React' })).toBeNull()
  })
})

describe('toQueryString', () => {
  it('only sends filters that are set', () => {
    expect(toQueryString({ overdue: false, sort: 'PRIORITY' })).toBe('sort=PRIORITY')
    expect(toQueryString({ status: 'DONE', priority: 'HIGH', overdue: true, sort: 'CREATION' })).toBe(
      'sort=CREATION&status=DONE&priority=HIGH&overdue=true',
    )
  })
})
