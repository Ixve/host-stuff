import * as Express from 'express';
import * as DomainController from '@/controllers/v1/client/settings/DomainController';
import * as SubdomainController from '@/controllers/v1/client/settings/SubdomainController';
import * as EmbedController from '@/controllers/v1/client/settings/EmbedController';
import SchemaMiddleware from '@/middleware/v1/SchemaMiddleware';
import DomainSchema from '@/schemas/v1/client/settings/DomainSchema';
import SubdomainSchema from '@/schemas/v1/client/settings/SubdomainSchema';
import EmbedSchema from '@/schemas/v1/client/settings/EmbedSchema';

const SettingsRouter: Express.Router = Express.Router();

SettingsRouter.get('/domains', DomainController.Index);
SettingsRouter.post('/domains', SchemaMiddleware(DomainSchema()), DomainController.Store);

SettingsRouter.get('/subdomains', SubdomainController.Index);
SettingsRouter.post('/subdomains', SchemaMiddleware(SubdomainSchema()), SubdomainController.Store);

SettingsRouter.post('/embed', SchemaMiddleware(EmbedSchema()), EmbedController.Store);

export default SettingsRouter;